package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.eneity.*;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TestController {
    @Autowired
    PictureLoadUtil pictureLoadUtil;
    @Autowired
    NoticeService noticeService;
    @Autowired
    SchoolNewsService schoolNewsService;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    CommentService commentService;
    @PostMapping("test")
    public String test(MultipartFile file){
        System.out.println(file);
        String list = pictureLoadUtil.loadPicture(file);
        Map<String,Object> map=new HashMap<>();
        map.put("pictureUrl",list);
        return CommunityUtil.getJSONString(200,"成功",map);
    }
    @GetMapping("/index")
    public String index(){
        List<Notice> twoNotice = noticeService.findTwoNotice();
        List<Notice> notices = twoNotice.subList(0, 2);
        List<SchoolNews> all = schoolNewsService.findAll();
        List<SchoolNews> schoolNews = all.subList(0, 2);
        List<Map<String,Object>> list=new ArrayList<>();
        for (Notice index:notices){
            Map<String,Object>map=new HashMap<>();
            Groups group = groupService.findGroupById(index.getGroupId());
            map.put("writer",userService.findUserById(index.getUserId()));
            List<Comment> comments = commentService.selectComment(1, index.getId(), 3);
            map.put("commentSize",comments.size());
            map.put("groupId",index.getGroupId());
            map.put("noticeId",index.getId());
            map.put("noticeTitle",index.getTitle());
            List<String>picture=new ArrayList<>();
            if (index.getPicture1()!=null) picture.add(index.getPicture1());
            if (index.getPicture2()!=null) picture.add(index.getPicture2());
            if (index.getPicture3()!=null) picture.add(index.getPicture3());
            map.put("noticePicture",picture);
            map.put("createTime",index.getCreateTime());
            map.put("context",index.getContext());
            map.put("groupName",group.getName());
           map.put("groupUrl",group.getHeadUrl());
           list.add(map);
        }
        List<Map<String,Object>> list1=new ArrayList<>();
        for (SchoolNews index:schoolNews){
            Map<String,Object>map=new HashMap<>();
            map.put("createTime",index.getUploadTime());
            map.put("context",index.getContent());
            List<String>picture=new ArrayList<>();
            if (index.getPicture1()!=null) picture.add(index.getPicture1());
            if (index.getPicture2()!=null) picture.add(index.getPicture2());
            if (index.getPicture3()!=null) picture.add(index.getPicture3());
            if (index.getPicture4()!=null) picture.add(index.getPicture4());
            if (index.getPicture5()!=null) picture.add(index.getPicture5());
            if (index.getPicture6()!=null) picture.add(index.getPicture6());
            if (index.getPicture7()!=null) picture.add(index.getPicture7());
            if (index.getPicture8()!=null) picture.add(index.getPicture8());
            if (index.getPicture9()!=null) picture.add(index.getPicture9());
            map.put("newsId",index.getId());
            map.put("newsPicture",picture);
            list1.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("notices",list);
        map.put("news",list1);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
