package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Hire;
import com.example.demo.eneity.User;
import com.example.demo.service.HireService;
import com.example.demo.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HireController {

    @Autowired
    HireService hireService;

    @Autowired
    UserService userService;

    @GetMapping("/hire")
    public String findAll(){
        List<Hire> all = hireService.findAll();
        List<Map<String,Object>>list=new ArrayList<>();
        for (Hire hire:all){
            Map<String,Object>map=new HashMap<>();
            map.put("hireId",hire.getId());
            map.put("name",hire.getName());
            map.put("createTime",hire.getCreateTime());
            User user = userService.findUserById(hire.getSellerId());
            map.put("userName",user.getUsername());
            map.put("headUrl",user.getHeadurl());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();

        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

    @PostMapping("/hire")
    public String addOne(String name,String picture,int userId){
        Hire hire = new Hire();
        hire.setName(name);
        hire.setPicture(picture);
        hire.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        hire.setSellerId(userId);
        hireService.addOne(hire);
        return CommunityUtil.getJSONString(100,"添加成功");
    }

    @GetMapping("myHire")
    public String myHire(int userId){
        List<Hire> myHire = hireService.findAMyHire(userId);
        List<Map<String,Object>>list=new ArrayList<>();
        for (Hire hire:myHire){
            Map<String,Object>map=new HashMap<>();
            map.put("picture",hire.getPicture());
            map.put("name",hire.getName());
            map.put("createTime",hire.getCreateTime());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

    @PutMapping("/hire")
    public String putHire(String name,String picture,int id,int userId){
        Hire hire = hireService.findById(id);
        if (hire.getSellerId()!=userId) return CommunityUtil.getJSONString(500,"非法访问");
        hireService.putHire(name, picture, id);
        return CommunityUtil.getJSONString(200,"修改成功");
    }

    @DeleteMapping("/hire")
    public String deleteHire(int id,int userId){
        Hire hire = hireService.findById(id);
        if (hire.getSellerId()!=userId) return CommunityUtil.getJSONString(500,"非法访问");
        hireService.deleteById(id);
        return CommunityUtil.getJSONString(200,"删除成功");
    }

}
