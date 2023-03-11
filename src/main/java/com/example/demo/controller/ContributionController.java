package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.eneity.Contribution;
import com.example.demo.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class ContributionController {
    @Autowired
    ContributionService contributionService;
    @Autowired
    PictureLoadUtil pictureLoadUtil;

    @PostMapping("/contribution")
    public String postNews(int userId, String content, @RequestParam List<String> pictures){
        if (pictures==null||pictures.size()<=0){
            return CommunityUtil.getJSONString(500,"请至少上传一张图片作为投稿");
        }
//        List<String> list = pictureLoadUtil.loadPictureList(files);
//        System.out.println(list);
        Contribution contribution = new Contribution();
        contribution.setContent(content);
        contribution.setStatus(0);
        if (pictures.size()>0) {
            contribution.setPicture1(pictures.get(0));
        }
        if (pictures.size()>1) {
            contribution.setPicture2(pictures.get(1));
        }
        if (pictures.size()>2) {
            contribution.setPicture3(pictures.get(2));
        }
        contribution.setUserId(userId);
        contribution.setSubmitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        contributionService.insertContribution(contribution);
        return CommunityUtil.getJSONString(200,"投稿成功");
    }
    @GetMapping(path = "/myContribution")
    public String myContribution(int userId){
        List<Contribution> list = contributionService.myContribution(userId);
        if(list.size()<=0){
            return CommunityUtil.getJSONString(500,"没有投稿信息");
        }
        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>>list1=new ArrayList<>();
        for (Contribution index:list){
            Map<String,Object>map1=new HashMap<>();
            map1.put("content",index.getContent());
            map1.put("status",index.getStatus());
            map1.put("submitTime",index.getSubmitTime());
            List<String>picture=new ArrayList<>();
            if (index.getPicture1()!=null)picture.add(index.getPicture1());
            if (index.getPicture2()!=null)picture.add(index.getPicture2());
            if (index.getPicture3()!=null)picture.add(index.getPicture3());
            map1.put("pictures",picture);
            list1.add(map1);
        }
        map.put("myContribution", list1);
        return CommunityUtil.getJSONString(200, "我的投稿", map);
    }
}
