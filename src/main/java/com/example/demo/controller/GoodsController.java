package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.HostHolder;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.Util.RecommendUtil;
import com.example.demo.eneity.*;
import com.example.demo.eneity.vo.GoodsVo;
import com.example.demo.event.EventProducer;
import com.example.demo.service.*;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class GoodsController {

    @Autowired
    HostHolder hostHolder;
    @Autowired
    GoodService goodService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    UserService userService;

    @Autowired
    PictureLoadUtil pictureLoadUtil;
    @Autowired
    WantedService wantedService;
    @Autowired
    PurchaserService purchaserService;

    @Autowired
    GoodPictureService goodPictureService;
    @Autowired
    MessageService messageService;


    @Value("${university.path.upload}")
    private String uploadPath;
    @Value("${cos.accessKey}")
    private String accessKey;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.bucket}")
    private String bucket;
    @Value("${cos.bucketName}")
    private String bucketName;
    @Value("${cos.path}")
    private String path;


    @GetMapping(path = "/good")
    //商品详情
    public String findGoodsById(int id, int userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.findUserById(userId);
        int count = wantedService.countWanted(id);
        Goods goods = goodService.findById(id);
        int flag = 0;
        Wanted wantByUserGoodId = wantedService.findWantByUserGoodId(userId, id);
        if (wantByUserGoodId == null) flag = 0;
        else flag = 1;
        List<GoodPictures> goodPicture = goodPictureService.findGoodPicture(id);
        List<String> list = new ArrayList<>();
        for (GoodPictures index : goodPicture) {
            list.add(index.getPictureUrl());
        }
        map.put("ifWant", flag);
        map.put("goodPictures", list);
        map.put("goods", goods);
        map.put("wantCount", count);
        map.put("sellerName", user.getUsername());
        map.put("sellerHeadurl", user.getHeadurl());
        if (userId == goods.getSellerId()) {
            map.put("update", 1);
        } else {
            map.put("update", 0);
        }
        if (goods != null) {
            return CommunityUtil.getJSONString(200, "商品信息", map);
        } else {
            return CommunityUtil.getJSONString(500, "该商品不存在");
        }
    }

    //    @GetMapping(path = "/goods")
//    //所有商品
//    public String findAll(@RequestParam(required = false,defaultValue = "1")int page){
//        Map<String,Object> map = new HashMap<>();
//        List<Goods> list = goodService.findAll();
//        int total;
//        if (list.size()%8==0)
//            total=list.size()/8;
//        else total=list.size()/8+1;
//        map.put("total",total);//总页数
//        if (page>total)return CommunityUtil.getJSONString(500,"非法访问");
//        if (page==total){
//            map.put("goods",list.subList((page-1)*8,list.size()));
//        }else{
//            map.put("goods",list.subList((page-1)*8,page*8));
//        }
//        map.put("Current",page);
//        return CommunityUtil.getJSONString(200,"商品信息",map);
//    }
    @PostMapping(path = "/goods")
//上架商品
    public String uploadGoods(Goods goods, int userId, MultipartFile file) {
        if (file.getOriginalFilename().length() <= 0) {
            return CommunityUtil.getJSONString(500, "请选择图片");
        }
        String path = pictureLoadUtil.loadPicture(file);
        goods.setPicture(path);
        goods.setStatus(1);
        goods.setSellerId(userId);
        goods.setUpload_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        goodService.insertGoods(goods);
        return CommunityUtil.getJSONString(200, "上架成功");
    }

    @PutMapping(path = "/goods")
//修改商品
    public String updateGoods(Goods goods, Integer userId, String cover, @RequestParam List<String> pictures) {
//    public String updateGoods(@RequestBody GoodsVo goodsVo){
        if (cover == null) {
            Goods byId = goodService.findById(goods.getId());
            cover = byId.getPicture();
        }
        String path = cover;
//        System.out.println(goods);
        List<GoodPictures> goodPicture = goodPictureService.findGoodPicture(goods.getId());
        if (goodPicture != null) {
            goodPictureService.deleteGoodPicture(goods.getId());
        }
        for (String index : pictures) {
            GoodPictures goodPictures = new GoodPictures();
            goodPictures.setGoodId(goods.getId());
            goodPictures.setPictureUrl(index);
            goodPictures.setUserId(userId);
            goodPictureService.addgoodPictures(goodPictures);
        }
        goods.setSellerId(userId);
        goods.setStatus(1);
        goods.setPicture(path);
        System.out.println(goods);
        goodService.updateGoods(goods);
        return CommunityUtil.getJSONString(200, "修改成功");
    }

    @GetMapping("purchase")
    //发送购买请求
    public String purchase(int goodId, int userId) {
//        User user = hostHolder.getUser();
        Goods good = goodService.findById(goodId);
        if (userId == good.getSellerId()) return CommunityUtil.getJSONString(500, "这是你自己的商品");
        User user = userService.findUserById(userId);
        if (goodService.findById(goodId) == null) {
            return CommunityUtil.getJSONString(500, "该商品已卖出或已被下架");
        }
        Wanted wanted = new Wanted();
        wanted.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        wanted.setGoodId(goodId);
        wanted.setSellerId(good.getSellerId());
        wanted.setUserId(userId);
        Wanted wantedById = wantedService.findWantByUserGoodId(userId, goodId);
        if (wantedById != null) {
            return CommunityUtil.getJSONString(500, "您已经发送过请求");
        }
        wantedService.addWanted(wanted);
        if (good == null) {
            return CommunityUtil.getJSONString(500, "该商品不存在");
        }
        if (good.getStatus() != 1) {
            return CommunityUtil.getJSONString(500, "商品已被购买或下架");
        }
        Event event = new Event();
        event.setEntityType(1);
        event.setEntityId(goodId);
        event.setTopic("purchase");
        event.setData("goodId", goodId);
        event.setData("goodName", good.getName());
        event.setData("goodUrl", good.getPicture());
        event.setEntityUserId(good.getSellerId());
        event.setUserId(userId);
        event.setData("type", "purchase");
        event.setData("username", user.getUsername());
        event.setData("headUrl", user.getHeadurl());
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(200, "请求发送成功，请耐心等待商家回应");
    }

    @PutMapping("purchase")
    //处理购买请求，goodId为商品id，userId为购买人Id
    public String purchase(int goodId, int userId, int status, int messageId) {
        Goods good = goodService.findById(goodId);
//        User user = hostHolder.getUser();
        User user = userService.findUserById(userId);
        if (good == null) return CommunityUtil.getJSONString(500, "该商品已不存在");
        if (good.getStatus() != 1) return CommunityUtil.getJSONString(500, "该商品已被拍下或已下架");
        Event event = new Event();
        //同意请求
        if (status == 1) {
            goodService.updateGoodStatus(goodId);
            event.setEntityType(1);
            event.setData("goodName", good.getName());
            event.setData("goodUrl", good.getPicture());
            event.setData("sellerName", userService.findUserById(good.getSellerId()).getUsername());
            event.setData("sellerUrl", userService.findUserById(good.getSellerId()).getHeadurl());
            event.setTopic("purchase");
            event.setEntityId(goodId);
            event.setEntityUserId(userId);
            event.setUserId(good.getSellerId());
            event.setData("type", "successful");
            eventProducer.fireEvent(event);
            Purchase purchase = new Purchase();
            purchase.setSellerId(good.getSellerId());
            purchase.setBuyerId(userId);
            purchase.setGoodId(goodId);
            purchase.setPurchaseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            purchaserService.addPurchase(purchase);
            messageService.deleteMessage(messageId);
        } else {
            event.setEntityType(1);
            event.setData("sellerName", userService.findUserById(good.getSellerId()).getUsername());
            event.setData("sellerUrl", userService.findUserById(good.getSellerId()).getHeadurl());
            event.setTopic("purchase");
            event.setEntityId(goodId);
            event.setEntityUserId(userId);
            event.setUserId(good.getSellerId());
            event.setData("goodName", good.getName());
            event.setData("goodUrl", good.getPicture());
            event.setData("type", "fail");
            eventProducer.fireEvent(event);
            messageService.deleteMessage(messageId);
        }
        return CommunityUtil.getJSONString(200, "处理成功");
    }

    @GetMapping(path = "/userCollection")
    public String userCollection(int userId) {
        User user = userService.findUserById(userId);
        String userCollection = user.getCollection();
        if (userCollection.length() > 1) {

            List list = new ArrayList();
            String str = userCollection + ",";
            String substring = str.substring(0, str.length() - 1);
            String[] result = substring.split(",");
            Map<String, Object> map = new HashMap<>();
            for (String r : result) {
                System.out.println("分割结果是: " + r);
                Goods goods = goodService.findById(Integer.parseInt(r));
                if (goods == null) continue;
                list.add(goods);
            }
            map.put("商品详情", list);
            System.out.println(map);
            return CommunityUtil.getJSONString(200, "收藏列表", map);
        } else
            return CommunityUtil.getJSONString(500, "收藏列表为空");
    }

    @DeleteMapping("goods")
    public String deleteGoods(int goodId, int userId) {
        Goods good = goodService.findById(goodId);
        User user = userService.findUserById(userId);
        if (user.getStatus() == 2) {
            if (good == null) return CommunityUtil.getJSONString(500, "商品已不存在");
            goodService.deleteGoods(goodId);
            return CommunityUtil.getJSONString(200, "删除成功");
        }
        if (good.getSellerId() != userId) {
            return CommunityUtil.getJSONString(500, "这不是你的商品");
        }
        goodService.deleteGoods(goodId);
        return CommunityUtil.getJSONString(200, "下架成功");
    }

    @GetMapping(path = "/obscureSelect")
    public String obscureSelect(String str, @RequestParam(required = false, defaultValue = "1") int page) {
        Map<String, Object> map = new HashMap<>();
        List<Goods> list = goodService.obscureSelect(str);
        int total;
        if (list.size() % 8 == 0)
            total = list.size() / 8;
        else total = list.size() / 8 + 1;
        map.put("total", total);//总页数
        if (page > total) return CommunityUtil.getJSONString(500, "非法访问");
        if (page == total) {
            map.put("goods", list.subList((page - 1) * 8, list.size()));
        } else {
            map.put("goods", list.subList((page - 1) * 8, page * 8));
        }
        map.put("Current", page);
        if (list != null) {
            return CommunityUtil.getJSONString(200, "商品信息", map);
        } else {
            return CommunityUtil.getJSONString(500, "该商品不存在");
        }
    }

    @GetMapping("/mayLike")
    public String findMayLike(int goodId) {
        Set<Goods> list = new HashSet<>();
        int maxId = goodService.findMaxId();
        int minId = goodService.findMinId();
        for (int i = 0; i < 4; i++) {
            while (true) {
                int x = (int) (Math.random() * (maxId - minId) + minId + 1);
                if (x != goodId && goodService.findById(x) != null && !list.contains(goodService.findById(x)) && goodService.findById(x).getStatus() == 1) {
                    list.add(goodService.findById(x));
                    System.out.println(x);
                    break;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("MayLike", list);
        return CommunityUtil.getJSONString(200, "mayLike", map);
    }

    @GetMapping("/myGood")
    public String findMyGood(int userId) {
        List<Goods> myGood = goodService.findMyGood(userId);
        Map<String, Object> map = new HashMap<>();
        List<Goods> list = new ArrayList<>();
        for (Goods goods : myGood) {
//            if (goods.getStatus()<=2)
            list.add(goods);
        }
        map.put("count", list.size());
        map.put("myGood", list);
        return CommunityUtil.getJSONString(200, "查询成功", map);
    }

    //商品标签
    @GetMapping(path = "/findByLabel")
    public String findByLabel(int label) {
        String goods[][] = {
                {"糖", "面", "肉", "饭", "零食", "奶", "茶"},
                {"运动鞋", "篮球鞋", "板鞋", "短裤", "裤", "男","牛仔裤男"},
                {"裙", "连衣", "牛仔裤女", "女"},
                {"笔", "书", "英语", "数学", "钢", "论", "讲义", "原理"},
                {"球", "哑铃", "球拍", "乒乓"},
                {"卡牌", "牌", "卡片"},
                {"手机", "电脑", "电", "显卡", "鼠标", "键盘", "平板", "硬盘", "游戏机","数码","相机","手柄","拍立得"}};
        String good[] = goods[label];
        System.out.println(good);
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();
        for (int i = 0; i < good.length; i++) {
            System.out.println(good[i]);
            list1 = goodService.findByLabel(good[i]);
            list2.addAll(list1);
        }
        System.out.println(list1);
        System.out.println(list2);
        Set<Integer> set = new HashSet<>();
        System.out.println(set);
        set.addAll(list2);
        if (set.size() <= 0) {
            return CommunityUtil.getJSONString(500, "没找到相关商品");
        }
        List<Integer> lists = new ArrayList<>(set);
        List<Goods> list = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
//            System.out.println(lists.get(i));
            Goods goods1 = goodService.findById(lists.get(i));
            if (goods1 == null) continue;
            list.add(goods1);
        }
//        if (page>total)return CommunityUtil.getJSONString(500,"非法访问");
        Map<String,Object>map=new HashMap<>();
            List<Map<String, Object>> goodss = new ArrayList<>();
            for (Goods index : list) {
                if (index.getStatus() == 3) continue;
                Map<String, Object> map1 = new HashMap<>();
                map1.put("id", index.getId());
                map1.put("price", index.getPrice());
                map1.put("sellerName", userService.findUserById(index.getSellerId()).getUsername());
                map1.put("picture", index.getPicture());
                map1.put("name", index.getName());
                map1.put("uploadDate", goodService.findDate(index.getId()));
                map1.put("wantCount", wantedService.countWanted(index.getId()));
                goodss.add(map1);
            }
            map.put("goods", goodss);
        return CommunityUtil.getJSONString(200, "商品详情", map);
    }

    //已卖出的商品
    @GetMapping("/soldGoods")
    public String soldGoods(int userId) {
        List<Purchase> purchases = purchaserService.findMySold(userId);
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Purchase index : purchases) {
            Goods goods = goodService.findById(index.getGoodId());
            if (goods==null)continue;
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", goods.getName());
            map1.put("soldTime", index.getPurchaseTime());
            map1.put("detail", goods.getDetail());
            map1.put("id", goods.getId());
            map1.put("picture", goods.getPicture());
            list.add(map1);
        }
        if (list.size() <= 0) {
            return CommunityUtil.getJSONString(500, "没有相关商品");
        }
        map.put("soldGoods", list);
        return CommunityUtil.getJSONString(200, "查询成功", map);
    }

    @GetMapping("/goods")
    public String recommend(int userId, @RequestParam(required = false, defaultValue = "1") int page) {
        User user = userService.findUserById(userId);
        List wantedList1 = wantedService.getWantedList(userId);
        if ((user.getCollection() == null || user.getCollection().length() < 1) && (wantedList1 == null || wantedList1.size() < 1)) {
            Map<String, Object> map = new HashMap<>();
            List<Goods> list = goodService.findAll();
            int total;
            if (list.size() % 8 == 0)
                total = list.size() / 8;
            else total = list.size() / 8 + 1;
            map.put("total", total);//总页数
//        if (page>total)return CommunityUtil.getJSONString(500,"非法访问");
            if (page == total) {
                List<Map<String, Object>> goods = new ArrayList<>();
                for (Goods index : list.subList((page - 1) * 8, list.size())) {
                    if (index.getStatus() == 3) continue;
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("id", index.getId());
                    map1.put("price", index.getPrice());
                    map1.put("sellerName", userService.findUserById(index.getSellerId()).getUsername());
                    map1.put("picture", index.getPicture());
                    map1.put("name", index.getName());
                    map1.put("uploadDate", goodService.findDate(index.getId()));
                    map1.put("wantCount", wantedService.countWanted(index.getId()));
                    goods.add(map1);
                }
                map.put("goods", goods);
            } else {
                List<Map<String, Object>> goods = new ArrayList<>();
                for (Goods index : list.subList((page - 1) * 8, page * 8)) {
                    Map<String, Object> map1 = new HashMap<>();
                    if (index == null) continue;
                    if (index.getStatus() == 3) continue;
                    map1.put("id", index.getId());
                    map1.put("price", index.getPrice());
                    map1.put("sellerName", userService.findUserById(index.getSellerId()).getUsername());
                    map1.put("picture", index.getPicture());
                    map1.put("name", index.getName());
                    map1.put("uploadDate", goodService.findDate(index.getId()));
                    map1.put("wantCount", wantedService.countWanted(index.getId()));
                    goods.add(map1);
                }
                map.put("goods", goods);
                map.put("Current", page);
                return CommunityUtil.getJSONString(200, "商品信息", map);
            }
        }
            RecommendUtil recommendUtil = new RecommendUtil();
            List<User> users = userService.findUsers();
            List<Goods> all = goodService.findAll();
            for (User index : users) {
                Map<Integer, Integer> map = new HashMap<>();
                if (index.getCollection() == null || index.getCollection().length() < 1) continue;
                int sorce = 0;
                String[] split = index.getCollection().split(",");
                for (String string : split) {
                    map.put(Integer.valueOf(string), 1);
                }
                List<Wanted> wantedList = wantedService.findWantedByUserId(index.getId());
                for (Wanted wanted : wantedList) {
                    if (map.get(wanted.getGoodId()) == null) {
                        map.put(wanted.getGoodId(), 2);
                    } else {
                        map.put(wanted.getGoodId(), 3);
                    }
                }
                for (Goods indexs : all) {
                    if (map.get(indexs.getId()) == null) {
                        map.put(indexs.getId(), 0);
                    }
                }
                for (Map.Entry<Integer, Integer> mm : map.entrySet()) {
                    index.set(mm.getKey(), mm.getValue());
                }//重新封装user 把list空的去掉
            }

            List<Goods> recommend = recommendUtil.recommend(userId, users);
            List<Goods> list = new ArrayList<>();
            for (Goods index : recommend) {
                list.add(goodService.findById(index.getId()));
            }
            int total;
            if (list.size() % 8 == 0)
                total = list.size() / 8;
            else total = list.size() / 8 + 1;
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);//总页数
            if (page > total) return CommunityUtil.getJSONString(500, "非法访问");
            if (page == total) {
                List<Map<String, Object>> goods = new ArrayList<>();
                for (Goods index : list.subList((page - 1) * 8, list.size())) {
                    if (index.getStatus() == 3) continue;
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("id", index.getId());
                    map1.put("price", index.getPrice());
                    map1.put("sellerName", userService.findUserById(index.getSellerId()).getUsername());
                    map1.put("picture", index.getPicture());
                    map1.put("name", index.getName());
                    map1.put("uploadDate", goodService.findDate(index.getId()));
                    map1.put("wantCount", wantedService.countWanted(index.getId()));
                    goods.add(map1);
                }
                map.put("goods", goods);
            } else {
                List<Map<String, Object>> goods = new ArrayList<>();
                for (Goods index : list.subList((page - 1) * 8, page * 8)) {
                    Map<String, Object> map1 = new HashMap<>();
                    if (index == null) continue;
                    if (index.getStatus() == 3) continue;
                    map1.put("id", index.getId());
                    map1.put("price", index.getPrice());
                    map1.put("sellerName", userService.findUserById(index.getSellerId()).getUsername());
                    map1.put("picture", index.getPicture());
                    map1.put("name", index.getName());
                    map1.put("uploadDate", goodService.findDate(index.getId()));
                    map1.put("wantCount", wantedService.countWanted(index.getId()));
                    goods.add(map1);
                }
                map.put("goods", goods);
            }
            map.put("Current", page);
            return CommunityUtil.getJSONString(200, "商品信息", map);
        }



    @PutMapping("/goodStatus")// 下架商品
    public String goodStatus(int goodId,int userId){
        Goods goods = goodService.findById(goodId);
        if (goods.getSellerId()!=userId) return CommunityUtil.getJSONString(500,"这不是你的商品");
        goodService.updateStatus(goodId);
        return CommunityUtil.getJSONString(200,"已卖出");
    }
    @GetMapping("/findPicture")
    public String findPicture(int goodId){
        Goods good = goodService.findById(goodId);
        Map<String,Object> map=new HashMap<>();
        map.put("goodUrl",good.getPicture());
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("/statisticsPur")
    public String statisticsPur(){
        List<Purchase> all = purchaserService.findAll();
        Map<String,Object>map=new HashMap<>();
        Set<String>list=new HashSet<>();
        List<Integer>list1=new ArrayList<>();
        for (int i=0;i<all.size();i++){
            all.get(i).setPurchaseTime(all.get(i).getPurchaseTime().substring(0,10));
            list.add(all.get(i).getPurchaseTime());
        }
        List<String>result=new ArrayList<>(list);
        Collections.sort(result);
        for (String index:result){
            list1.add(purchaserService.count(index));
        }
        Map<String,Object>map1=new HashMap<>();
        map1.put("x",result);
        map1.put("y",list1);
        return CommunityUtil.getJSONString(200,"查询成功",map1);
    }
}
