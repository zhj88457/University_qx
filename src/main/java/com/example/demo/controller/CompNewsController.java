package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.dao.CompNewsMapper;
import com.example.demo.eneity.CompNews;
import com.example.demo.service.CompNewsService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CompNewsController {
   @Autowired
   CompNewsService compNewsService;

   @GetMapping("/CompNews")
   public String findAllNews(int userId){
       List<CompNews> list=compNewsService.findAll();
       List<Map<String,Object>>list1=new ArrayList<>();
       for (int i = 0; i < list.size(); i++) {
           Map<String,Object>map=new HashMap<>();
           map.put("title",list.get(i).getTitle());
           map.put("status",list.get(i).getStatus());
           map.put("content",list.get(i).getContent());
           map.put("picture1",list.get(i).getPicture1());
           map.put("picture2",list.get(i).getPicture2());
           map.put("picture3",list.get(i).getPicture3());
           map.put("createTime",list.get(i).getCreateTime());
           map.put("count",list.get(i).getCount());
           list1.add(map);
       }
       Map<String,Object>map=new HashMap<>();
       map.put("result",list1);
       return CommunityUtil.getJSONString(200,"查询成功",map);
   }

    @GetMapping("/findByStatus")
    public String findByStatus(int userId,int status){
        List<CompNews> byStatus = compNewsService.findByStatus(status);
        List<Map<String,Object>>mapList=new ArrayList<>();
        for (CompNews compNews:byStatus){
            Map<String,Object> map=new HashMap<>();
            map.put("title",compNews.getTitle());
            map.put("status",compNews.getStatus());
            map.put("content",compNews.getContent());
            map.put("picture1",compNews.getPicture1());
            map.put("picture2",compNews.getPicture2());
            map.put("picture3",compNews.getPicture3());
            map.put("createTime",compNews.getCreateTime());
            map.put("count",compNews.getCount());
            mapList.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",mapList);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

    @GetMapping("/findById")
    public String findById(int userId,int id){
       compNewsService.updateCount(id);
        CompNews byId = compNewsService.findById(id);
        if (byId==null) return CommunityUtil.getJSONString(500,"该新闻不存在");
        Map<String,Object>map=new HashMap<>();
        map.put("result",byId);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

}
