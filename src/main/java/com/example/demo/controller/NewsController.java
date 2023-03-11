package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Comment;
import com.example.demo.eneity.Contribution;
import com.example.demo.eneity.SchoolNews;
import com.example.demo.service.CommentService;
import com.example.demo.service.ContributionService;
import com.example.demo.service.SchoolNewsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class NewsController {
    @Autowired
    ContributionService contributionService;
    @Autowired
    SchoolNewsService schoolNewsService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @GetMapping(path = "/getAllNews")
    public String getAllNews() {
        List<Contribution> list = contributionService.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("newsList", list);
        return CommunityUtil.getJSONString(200, "全部投稿", map);
    }

    @PostMapping(path = "/checkedNews")
    public String checkedNews(SchoolNews schoolNews) {
        System.out.println(schoolNews);
        if (schoolNews != null) {
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            schoolNews.setUploadTime(df.format(day));
            System.out.println(schoolNews.getContent());
            schoolNewsService.addSchoolnews(schoolNews);
            return CommunityUtil.getJSONString(200, "审核通过");
        } else
            return CommunityUtil.getJSONString(500, "请选择图片");
    }

    @GetMapping(path = "/getCheckedNews")
    public String getCheckedNews() {
        List<SchoolNews> list = schoolNewsService.findAll();
//        System.out.println(list);
        List<Map<String,Object>>lists=new ArrayList<>();
        Map<String,Object>mapEnd=new HashMap<>();
        for (SchoolNews schoolNews : list) {
            Map<String, Object> mapResult = new HashMap<>();
            List<String> list1 = new ArrayList<>();
            list1.add(schoolNews.getPicture1());
            list1.add(schoolNews.getPicture2());
            list1.add(schoolNews.getPicture3());
            list1.add(schoolNews.getPicture4());
            list1.add(schoolNews.getPicture5());
            list1.add(schoolNews.getPicture6());
            list1.add(schoolNews.getPicture7());
            list1.add(schoolNews.getPicture8());
            list1.add(schoolNews.getPicture9());
            List<Comment> comments = commentService.selectComment(1, schoolNews.getId(), 2);
            List<Comment>list2=commentService.findComment3(1, schoolNews.getId(), 2);
            List<Map<String,Object>>list3=new ArrayList<>();
            for (Comment index:list2){
                Map<String ,Object>map=new HashMap<>();
                map.put("content",index.getContent());
                map.put("commentUsername",userService.findUserById(index.getFromId()).getUsername());
                map.put("commentUserUrl",userService.findUserById(index.getFromId()).getHeadurl());
                map.put("commentTime",index.getCommentTime());
                list3.add(map);
            }
            mapResult.put("comment",list3);
            mapResult.put("commentCount",comments.size());
            mapResult.put("imageList", list1);
            mapResult.put("content", schoolNews.getContent());
            mapResult.put("id", schoolNews.getId());
            mapResult.put("uploadTime", schoolNews.getUploadTime());
            mapResult.put("status",schoolNews.getStatus());
            lists.add(mapResult);
        }
        mapEnd.put("result",lists);
            return CommunityUtil.getJSONString(200, "显示成功", mapEnd);

        }

    }
