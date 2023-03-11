package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.HostHolder;
import com.example.demo.eneity.Goods;
import com.example.demo.eneity.User;
import com.example.demo.service.CollectService;
import com.example.demo.service.GoodService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class CollectController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CollectService collectService;
    @Autowired
    UserService userService;
    @Autowired
    GoodService goodService;
    @PostMapping(path = "/collection")
    public String collectGoods(int id,int userId){
        User user = userService.findUserById(userId);
        String collection1 = user.getCollection();
        String []  con = collection1.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(con));
        String ids = String.valueOf(id);
        set.add(ids);
        String collection = String.join(",",set);
        String stunum = user.getStunum();
        collectService.collectGoods(stunum,collection);
        return CommunityUtil.getJSONString(200,"收藏成功");
    }

    @GetMapping(path = "/collection")
    public String isCollectGoods(int id,int userId) {
        User user = userService.findUserById(userId);
        System.out.println(user);
        if (user.getCollection()==null||user.getCollection().length()<1) {
            String goodsId = id + ",";
            collectService.collectGoods(user.getStunum(), goodsId);
            return CommunityUtil.getJSONString(200, "收藏成功");
        } else {
            String collection1 = user.getCollection();
            System.out.println(collection1);
            String[] con = collection1.split(",");
            Set<String> set = new HashSet<>(Arrays.asList(con));
            String ids = String.valueOf(id);
            boolean collect = set.contains(ids);
            String stunum = user.getStunum();
            if (collect) {
                set.remove(ids);
                String collection = String.join(",", set);
                collectService.collectGoods(stunum, collection);
                return CommunityUtil.getJSONString(200, "取消收藏");
            } else {
                set.add(ids);
                String collection = String.join(",", set);
                collectService.collectGoods(stunum, collection);
                return CommunityUtil.getJSONString(200, "收藏成功");
            }
        }
    }
    @GetMapping(path = "/isCollection")
    public String whetherCollection(int id,int userId){
        User user = userService.findUserById(userId);
        String collection1 = user.getCollection();
        System.out.println(collection1);
        String[] con = collection1.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(con));
        String ids = String.valueOf(id);
        boolean collect = set.contains(ids);
        if (collect) {
            return CommunityUtil.getJSONString(200, "1");
        } else {
            return CommunityUtil.getJSONString(200, "0");
        }
    }


}
