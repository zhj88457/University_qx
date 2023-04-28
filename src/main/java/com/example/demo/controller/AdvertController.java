package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.dao.AdvertMapper;
import com.example.demo.eneity.Advert;
import com.example.demo.eneity.Comment;
import com.example.demo.service.AdvertService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class AdvertController {
    @Autowired
    AdvertService advertService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @PostMapping("/advert")
    public String AddAdvert(Advert advert,@RequestParam List<String> pictures){
        if (advert.getDescribes()==null||advert.getDescribes().length()==0) return CommunityUtil.getJSONString(500,"描述不可为空");
        if (pictures.size()>0)
            advert.setPicture1(pictures.get(0));
        if (pictures.size()>1)
            advert.setPicture2(pictures.get(1));
        if (pictures.size()>2)
            advert.setPicture3(pictures.get(2));
        if (pictures.size()>3)
            advert.setPicture4(pictures.get(3));
        advert.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        advertService.addAdvert(advert);
        return CommunityUtil.getJSONString(200,"添加成功");
    }

    @GetMapping("/advert")
    public String selectAll(){
        List<Advert>list=advertService.selectAll();
        Map<String,Object>map=new HashMap<>();
        List<Map<String,Object>>reList=new ArrayList<>();
        for (Advert advert:list){
            Map<String,Object>result=new HashMap<>();
            List<String>list1=new ArrayList<>();
            list1.add(advert.getPicture1());
            list1.add(advert.getPicture2());
            list1.add(advert.getPicture3());
            list1.add(advert.getPicture4());
            result.put("pictures",list1);
            List<Comment> comment3 = commentService.findComment3(1, advert.getId(), 5);
            List<Comment> comments = commentService.selectComment(1, advert.getId(), 5);
            List<Map<String,Object>>maps=new ArrayList<>();
            for(Comment comment:comment3){
                Map<String,Object>map1=new HashMap<>();
                map1.put("content",comment.getContent());
                map1.put("commentUsername",userService.findUserById(comment.getFromId()).getUsername());
                map1.put("commentUserUrl",userService.findUserById(comment.getFromId()).getHeadurl());
                map1.put("createTime",comment.getCommentTime());
                maps.add(map1);
            }
            result.put("comment",maps);
            result.put("commentCount",comments.size());
            result.put("describe",advert.getDescribes());
            result.put("advertUsername",userService.findUserById(advert.getUserId()).getUsername());
            result.put("advertUserUrl",userService.findUserById(advert.getUserId()).getHeadurl());
            reList.add(result);
        }
        map.put("result",reList);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("/myAdvert")
    public String myAdvert(int userId){
        List<Advert> list = advertService.selectMy(userId);
        if (list.size()==0) return CommunityUtil.getJSONString(250,"您暂未发布闲置帖子");
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

    @PutMapping("/advert")
    public String putAdvert(String describes,
                            @RequestParam List<String> pictures,
                            int id,
                            int userId){
        Advert advert = advertService.findById(id);
        String picture1 = null,picture2 = null,picture3=null,picture4=null;
        if (pictures.size()>0)
       picture1 = pictures.get(0);
        if (pictures.size()>1)
            picture2=pictures.get(1);
        if (pictures.size()>2)
            picture3=pictures.get(2);
        if (pictures.size()>3)
            picture4=pictures.get(3);
        if (advert.getUserId()!=userId) return CommunityUtil.getJSONString(500,"非法访问");
        advertService.putAdvert(describes, picture1, picture2, picture3, picture4, id);
        return CommunityUtil.getJSONString(200,"修改成功");
    }

    @DeleteMapping("/advert")
    public String deleteById(int id,int userId){
        Advert byId = advertService.findById(id);
        if (byId.getUserId()!=userId) return CommunityUtil.getJSONString(500,"非法访问");
        advertService.deleteById(id);
        return CommunityUtil.getJSONString(200,"删除成功");
    }
}
