package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.eneity.Production;
import com.example.demo.eneity.User;
import com.example.demo.service.LikeService;
import com.example.demo.service.ProductionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class ProductionController {
    @Autowired
    LikeService likeService;
    @Autowired
    @Qualifier("redisTemplate1")
    RedisTemplate redisTemplate;
    @Autowired
    ProductionService productionService;
    @GetMapping("/production")
    public String getProductions(int userId){
//        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        Set<ZSetOperations.TypedTuple<Integer>> tuples = new HashSet<>();
        List<Integer> listIds = productionService.findAllProduction();
        for (Integer index:listIds){
            DefaultTypedTuple<Integer> tuple = new DefaultTypedTuple<>(index, 1D* likeService.findEntityLikeCount(2,index));
            tuples.add(tuple);
//            System.out.println( likeService.findEntityLikeCount(2,index));
        }
        Long add = redisTemplate.opsForZSet().add("sort", tuples);
        Set<ZSetOperations.TypedTuple<Integer>> set = redisTemplate.opsForZSet().reverseRangeWithScores("sort", 0, 100);
        System.out.println(set);
        List<Map<String,Object>>list=new ArrayList<>();
        for (ZSetOperations.TypedTuple<Integer> index:set){
            Map<String,Object>map=new HashMap<>();
            System.out.println(index.getValue());
            Production production = productionService.findProductionById(index.getValue());
            System.out.println(production);
            if (production==null||production.getStatus()==0) continue;
            map.put("count",index.getScore().intValue());
            map.put("shopName",production.getShopName());
            map.put("tradeName",production.getName());
            map.put("status",likeService.findEntityLikeStatus(userId,2,index.getValue()));
            map.put("location",production.getLocation());
            map.put("price",production.getPrice());
            map.put("picture",production.getPicture());
            map.put("productionId",production.getId());
            list.add(map);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("rank",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @DeleteMapping("production")
    public String deleteProduction(int pro_id){
        Production productionById = productionService.findProductionById(pro_id);
        if (productionById==null){
            return CommunityUtil.getJSONString(500,"产品已不存在，请重新刷新页面");

        }
        productionService.deleteProduction(pro_id);
        return CommunityUtil.getJSONString(200,"删除成功");
    }

    @Autowired
    UserService userService;
    @Autowired
    PictureLoadUtil pictureLoadUtil;

    @PostMapping(path = "/uploadFood")
    public String uploadFood(int userId, Production production, MultipartFile file){
        if(production !=null&&file.getOriginalFilename().length()>0){
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String path = pictureLoadUtil.loadPicture(file);
            production.setPicture(path);
            production.setUserId(userId);
            production.setUploadTime(df.format(day));
            production.setStatus(1);
            productionService.uploadFood(production);
            return CommunityUtil.getJSONString(200, "上传成功");
        }
        return CommunityUtil.getJSONString(500,"请选择上传内容");
    }
    @GetMapping(path = "/getAllFood")
    public String getAllFood(){
        Production production = new Production();
        List<Production> list = productionService.findAll();
        Map<String,Object> map = new HashMap<>();
        map.put("contributionList",list);
        return (String) CommunityUtil.getJSONString(200,"菜品信息",map);
    }
    @GetMapping(path = "/getFoodById")
    public String getFoodById(int proId){
        Production production = productionService.findProductionById(proId);
        if (production==null) {
            return CommunityUtil.getJSONString(500,"找不到该商品");
        }
        User user = userService.findUserById(production.getUserId());
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        map.put("shopName", production.getShopName());
        map.put("picture",production.getPicture());
        map.put("name",production.getName());
        map.put("price",production.getPrice());
        map.put("location",production.getLocation());
        map.put("detail",production.getDetail());
        map.put("userName",user.getUsername());
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        map1.put("dish",list);
        return (String) CommunityUtil.getJSONString(200, "菜品信息", map1);

    }
    @PutMapping(path = "/checkedDish")
    public String checkedDish (int proId){
        productionService.updateStatus(proId);
        return CommunityUtil.getJSONString(200, "审核通过");
    }
    //食堂标签查询
    @GetMapping(path = "/findProductionByLabel")
    public String findProductionByLabel(int label,int userId,int canteenId){
        String canteen="";
        if(canteenId==0){
            canteen="知辛苑";
        }else if (canteenId==1){
            canteen="东餐厅";
        }else if (canteenId==2){
            canteen="西餐厅";
        }else if (canteenId==3){
            canteen="小木屋";
        }
        String productions[][]={
                {"汤面","粉","拌面","面","水饺","火锅"},//0为面，1为米饭，2为饮料，3为烧烤，4为甜点，5为汉堡，6为鱼虾，7为早点,8为西餐,9为小吃,10为水果,11为减脂餐
                {"盖浇饭","饭","自选餐"},
                {"可乐","雪碧","汁","气泡","茶","露","咖啡","拿铁","卡布其诺","美式"},
                {"串","烤","铁板"},
                {"蛋糕","泡芙","面包","奶油","冰皮","甜点"},
                {"汉堡","炸鸡","炸"},
                {"鱼","虾"},
                {"豆","胡辣汤","生煎","包","油","饼","粥"},
                {"牛排","意面","披萨","土豆泥","派"},
                {"鸡米花","薯条","派","炸"},
                {"瓜","果","桃","柠檬","捞"},
                {"鸡胸肉","全麦面包","西蓝花","苏打水"}
                };
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();
        List<Integer> list3=new ArrayList<>();
        list3=productionService.findByCantten(canteen);
        List origin = new ArrayList<>();
        origin.addAll(list3);
        if (label>=0){
            String production[] = productions[label];
            for (int i=0;i<production.length;i++)
            {
                list1 = productionService.findByLabel(production[i]);
                list2.addAll(list1);
            }
            origin.retainAll(list2);
        }
        Set<Integer> set = new HashSet<>();
        set.addAll(origin);
        if(set.size()<=0){
            return CommunityUtil.getJSONString(500,"没找到相关菜品");
        }
        List<Integer>lists=new ArrayList<>(set);
        List<Production>list=new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Integer>> tuples = new HashSet<>();
        for (Integer index:lists){
            DefaultTypedTuple<Integer> tuple = new DefaultTypedTuple<>(index, 1D* likeService.findEntityLikeCount(2,index));
            tuples.add(tuple);
        }
        Long add = redisTemplate.opsForZSet().add("sorts"+canteen+String.valueOf(label), tuples);
        Set<ZSetOperations.TypedTuple<Integer>> sets = redisTemplate.opsForZSet().reverseRangeWithScores("sorts"+canteen+String.valueOf(label), 0, 100);
        List<Map<String,Object>>listd=new ArrayList<>();
        for (ZSetOperations.TypedTuple<Integer> index:sets){
            Map<String,Object>map=new HashMap<>();
            Production productioned = productionService.findProductionById(index.getValue());
            if (productioned==null||productioned.getStatus()==0) continue;
            map.put("count",index.getScore().intValue());
            map.put("shopName",productioned.getShopName());
            map.put("location",productioned.getLocation());
            map.put("tradeName",productioned.getName());
            map.put("status",likeService.findEntityLikeStatus(userId,2,index.getValue()));
            map.put("price",productioned.getPrice());
            map.put("picture",productioned.getPicture());
            map.put("productionId",productioned.getId());
            listd.add(map);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("labelProduction",listd);
        return CommunityUtil.getJSONString(200,"菜品详情",map);
    }
    @GetMapping("myProduction")
    public String findMyProduction(int userId){
        List<Production> myProduction = productionService.findMyProduction(userId);
        List<Map<String,Object>>list=new ArrayList<>();
        for (Production index:myProduction){
            Map<String,Object> map=new HashMap<>();
            map.put("proName",index.getName());
            map.put("proLocate",index.getLocation());
            map.put("proPrice",index.getPrice());
            map.put("likeCount",likeService.findEntityLikeCount(2,index.getId()));
            map.put("proId",index.getId());
            map.put("proDetail",index.getDetail());
            map.put("uploadTime",index.getUploadTime());
            map.put("shopName",index.getShopName());
            map.put("proUrl",index.getPicture());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("/statisticsPro")
    public String statisticsPro(){
        String productions[][]={
                {"汤面","粉","拌面","面","水饺","火锅"},//0为面，1为米饭，2为饮料，3为烧烤，4为甜点，5为汉堡，6为鱼虾，7为早点,8为西餐,9为小吃,10为水果,11为减脂餐
                {"盖浇饭","饭","自选餐"},
                {"可乐","雪碧","汁","气泡","茶","露","咖啡","拿铁","卡布其诺","美式"},
                {"串","烤","铁板"},
                {"蛋糕","泡芙","面包","奶油","冰皮","甜点"},
                {"汉堡","炸鸡","炸"},
                {"鱼","虾"},
                {"豆","胡辣汤","生煎","包","油","饼","粥"},
                {"牛排","意面","披萨","土豆泥","派"},
                {"鸡米花","薯条","派","炸"},
                {"瓜","果","桃","柠檬","捞"},
                {"鸡胸肉","全麦面包","西蓝花","苏打水"}
        };
        Map<String,Object>map=new HashMap<>();
        for (String[]index:productions){
            int count=0;
            for (int i=0;i<index.length;i++){
                List<Integer> byLabel = productionService.findByLabel(index[i]);
                for (int j=0;j<byLabel.size();j++){
                    count+=likeService.findEntityLikeCount(2,byLabel.get(j));
                }
            }
            if (index[0].equals("汤面")){
                map.put("mianlei",count);
            }else if (index[0].equals("盖浇饭")){
                map.put("mifan",count);
            }else if (index[0].equals("可乐")){
                map.put("yinliao",count);
            }else if (index[0].equals("串")){
                map.put("shaokao",count);
            }else if (index[0].equals("蛋糕")){
                map.put("tiandian",count);
            }else if (index[0].equals("汉堡")){
                map.put("hanbao",count);
            }else if (index[0].equals("鱼")){
                map.put("yuxia",count);
            }else if (index[0].equals("豆")){
                map.put("zaocan",count);
            }else if (index[0].equals("牛排")){
                map.put("xican",count);
            }else if (index[0].equals("鸡米花")){
                map.put("xiaochi",count);
            }else if (index[0].equals("瓜")){
                map.put("shuiguo",count);
            }else if (index[0].equals("鸡胸肉")){
                map.put("jianzhican",count);
            }
        }
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
