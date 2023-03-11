package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Goods;
import com.example.demo.eneity.Wanted;
import com.example.demo.service.GoodService;
import com.example.demo.service.UserService;
import com.example.demo.service.WantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class WantedController {
    @Autowired
    WantedService wantedService;
    @Autowired
    GoodService goodsService;
    @Autowired
    UserService userService;
    @PostMapping(path = "/wanted")
    public String addWanted(int goodId,int sellerId,int userId){
        if(goodsService.findById(goodId)==null){
            return CommunityUtil.getJSONString(500,"该商品已卖出或已被下架");
        }
        Wanted wanted = new Wanted();
        wanted.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        wanted.setGoodId(goodId);
        wanted.setSellerId(sellerId);
        wanted.setUserId(userId);
        wantedService.addWanted(wanted);
        return CommunityUtil.getJSONString(200,"已添加到我想要列表");
    }
    @GetMapping (path = "/getWantedList")
    public String getWantedList(int userId){
        if(wantedService.getWantedList(userId).size()<=0){
            return CommunityUtil.getJSONString(500,"未找到相关商品");
        }
        List<Wanted> wantedList = wantedService.getWantedList(userId);
        List<Goods>list=new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        for (Wanted wanted : wantedList){
            Goods goods = goodsService.findById(wanted.getGoodId());
            if (goods==null)continue;
            list.add(goods);
        }
        map.put("wantedList",list);
        return CommunityUtil.getJSONString(200,"商品列表",map);
    }
    @DeleteMapping("myWant")
    public String ignoreWant(int userId,int goodId){
        Wanted wantByUserGoodId = wantedService.findWantByUserGoodId(userId, goodId);
        if (wantByUserGoodId==null) return CommunityUtil.getJSONString(500,"你没有想要过它");
        wantedService.deleteWant(userId, goodId);
        return CommunityUtil.getJSONString(200,"取消成功");
    }
}
