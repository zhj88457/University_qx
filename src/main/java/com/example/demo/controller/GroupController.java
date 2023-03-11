package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.eneity.Groups;
import com.example.demo.eneity.Member;
import com.example.demo.eneity.User;
import com.example.demo.service.GroupService;
import com.example.demo.service.MemberService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    PictureLoadUtil pictureLoadUtil;
    @Autowired
    MemberService memberService;
    @Autowired
    UserService userService;
    //创建社团
    @PostMapping(path = "/createGroup")
    public String createGroup(Groups group, MultipartFile file, int userId){
        if (file.getOriginalFilename().length()<=0){
            return CommunityUtil.getJSONString(500,"请选择图片");
        }
        String path = pictureLoadUtil.loadPicture(file);
        group.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        group.setStatus(0);
        group.setHeadUrl(path);
        group.setUserId(userId);
        groupService.createGroup(group);
        System.out.println(group);
        return CommunityUtil.getJSONString(200,"申请成功");
    }
    //管理员端社团列表
    @GetMapping(path = "/adminGroups")
    public String adminGroups(){
        List<Groups> list = groupService.findAll();
        Map<String,Object> map = new HashMap<>();
        map.put("groups",list);
        return CommunityUtil.getJSONString(200,"社团列表",map);
    }
    //审核/请求解散/删除社团
    @PutMapping(path = "/checkedGroup")
    public String checkedGroup(int groupId,int status){
        int count = groupService.countGroupById(groupId);
        if(count==0){
            return CommunityUtil.getJSONString(404,"该社团不存在");
        }
        if(status==1){
            groupService.updateStatus(status,groupId);
            Groups group = groupService.findGroupById(groupId);
            Member member = new Member();
            member.setGroupId(groupId);
            member.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            member.setStatus(2);
            member.setUserId(group.getUserId());
            memberService.addMember(member);
            return CommunityUtil.getJSONString(200,"操作成功");
        }else
            groupService.updateStatus(status,groupId);
        return CommunityUtil.getJSONString(200,"操作成功");
    }
    //查全部已审核的社团
    @GetMapping(path = "/groups")
    public String groups(){
        List<Groups> list = groupService.findAllByStatus();
        Map<String,Object> map = new HashMap<>();
        map.put("groups",list);
        return CommunityUtil.getJSONString(200,"社团列表",map);
    }
    //查我关于我的社团（0为普通用户，1为当前用户为管理员的社团，2为当前用户为社长的社团）
    @GetMapping(path = "/myGroups")
    public String myGroups(int userId){
        List<Member> member = memberService.findMemberByUserId(userId);
        System.out.println(member);
        List<Map<String,Object>>list=new ArrayList<>();
      for (Member index:member){
          Map<String,Object> map=new HashMap<>();
          Groups group = groupService.findGroupById(index.getGroupId());
          if (group==null) continue;
          map.put("takePartTime",index.getCreateTime());
          map.put("groupName",group.getName());
          map.put("groupUrl",group.getHeadUrl());
          map.put("status",index.getStatus());
          map.put("groupDetail",group.getDetail());
          map.put("contact",group.getContact());
          map.put("createTime",group.getCreateTime());
          map.put("groupId",group.getId());
          list.add(map);
      }
      Map<String,Object> map=new HashMap<>();
      map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    //修改社团简介（仅社团一级管理员可进行）
    @PutMapping(path = "/changeDetail")
    public String changeDetail(int userId,int groupId,String detail){
        Member member = memberService.findMember(userId, groupId);
//        Groups group = groupService.findGroup(userId,groupId);
        if(member==null||member.getStatus()<=1){
            return CommunityUtil.getJSONString(500,"没有该权限");
        }
        else groupService.updateDetail(detail, groupId);
        return CommunityUtil.getJSONString(200,"修改成功");
    }
    //判断是否为管理员
    @GetMapping(path = "/isAdmin")
    public String isAdmin(int groupId,int userId){
        Member member = memberService.findById(groupId,userId);
        Map<String,Object> map = new HashMap<>();
        if(member==null){
            map.put("status",-1);
            return CommunityUtil.getJSONString(200,"该社团没有该用户",map);
        }

        if(member.getStatus()==1){
            map.put("status",1);
            return CommunityUtil.getJSONString(200,"管理员",map);
        }
        else if (member.getStatus()==2){
            map.put("status",2);
        return CommunityUtil.getJSONString(200,"社长",map);
        }
        else if (member.getStatus()==0) {
            map.put("status",0);
            return CommunityUtil.getJSONString(200,"普通成员",map);
        }
        else {
            map.put("status",-1);
            return CommunityUtil.getJSONString(200,"未加入",map);
        }
    }
    //成员列表
    @GetMapping(path = "/getMembersList")
    public String getMembersList(int groupId){
        if (groupService.findGroupById(groupId)==null){
            return CommunityUtil.getJSONString(500,"没有该社团");
        }
        List<Integer> members = memberService.findAll(groupId);
        System.out.println(members);
        List<Map<String,Object>>list=new ArrayList<>();
        for (int i=0;i<members.size();i++){
            System.out.println(members.get(i));
            User user = userService.findUserById(members.get(i));
            if (user==null) continue;
            Map<String,Object> map = new HashMap<>();
            map.put("status",memberService.findMember(members.get(i),groupId).getStatus());
            map.put("tel",user.getTel());
            map.put("stuNum",user.getStunum());
            map.put("school",user.getSchool());
            map.put("UserId",user.getId());
            map.put("UserName",user.getUsername());
            map.put("Headurl",user.getHeadurl());
            list.add(map);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("membersList",list);
        return CommunityUtil.getJSONString(200,"社团成员列表",map);
    }

}
