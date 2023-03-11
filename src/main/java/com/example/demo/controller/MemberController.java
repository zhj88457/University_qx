package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Groups;
import com.example.demo.eneity.Member;
import com.example.demo.eneity.Message;
import com.example.demo.eneity.User;
import com.example.demo.service.GroupService;
import com.example.demo.service.MemberService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    GroupService groupService;

    @PostMapping("/Member")
    public String addMember(int userId,int groupId){
        Member member = memberService.findMember(userId, groupId);
        if (member!=null&&member.getStatus()>=0) return CommunityUtil.getJSONString(500,"您已加入这个社团");
        if (member!=null&&member.getStatus()==-2) return CommunityUtil.getJSONString(500,"您已提交申请");
        if (member!=null&&member.getStatus()==-1) memberService.deleteMember(userId, groupId);
        Member m1 = new Member();
        m1.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        m1.setGroupId(groupId);
        m1.setStatus(-2);
        m1.setUserId(userId);
        memberService.insertMember(m1);
        return CommunityUtil.getJSONString(200,"请求成功，请耐心等待");
    }
    @PutMapping("/authorize")
    public String authorize(int userId,int groupId,int targetId,int status){
        Message message = new Message();
        message.setFromId(50);
        message.setMsgType(1);
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        message.setStatus(0);
        message.setToId(targetId);
        if (status==-2) {
            memberService.deleteMember(targetId,groupId);
            return CommunityUtil.getJSONString(200,"拒绝成功");
        }
        Member member = memberService.findMember(userId, groupId);
        if (member==null||member.getStatus()<1) return CommunityUtil.getJSONString(500,"你的权限不够用");
        if (status==2){
            if (member.getStatus()<2) return CommunityUtil.getJSONString(500,"你的权限不够");
            memberService.updateMember(userId,groupId,0);
            message.setContent("您已被设为"+groupService.findGroupById(groupId).getName()+"的社长");
        }
        else if (status==1){
            message.setContent("您已被设为"+groupService.findGroupById(groupId).getName()+"的管理员");
        }else if (status==0){
            message.setContent("您已成为"+groupService.findGroupById(groupId).getName()+"的一员");
        }
        memberService.updateMember(targetId,groupId,status);
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(200,"授权成功");
    }
    @DeleteMapping("/exile")
    public String exile(int userId,int groupId,int targetId){
        Message message = new Message();
        message.setFromId(50);
        message.setMsgType(1);
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        message.setStatus(0);
        message.setToId(targetId);
        Member member = memberService.findMember(userId, groupId);
        if (member==null||member.getStatus()<1) return CommunityUtil.getJSONString(500,"你的权限不够用");
        Member member1 = memberService.findMember(targetId, groupId);
        if (member.getStatus()>member1.getStatus()){
            memberService.deleteMember(targetId,groupId);
            message.setContent("您已被踢出"+groupService.findGroupById(groupId).getName());
            messageService.addMessage(message);
            return CommunityUtil.getJSONString(200,"删除成功");
        }
        return CommunityUtil.getJSONString(500,"你的权限不够");
    }
    @DeleteMapping("/quit")
    public String quit(int userId,int groupId){
        Member member = memberService.findMember(userId, groupId);
        if (member.getStatus()==2)return CommunityUtil.getJSONString(500,"请先移交会长位置");
        memberService.deleteMember(userId,groupId);
        return CommunityUtil.getJSONString(200,"退出成功");
    }
    @GetMapping("/appFrom")
    public String appFrom(int groupId,int userId){
        Member member = memberService.findMember(userId, groupId);
        if (member.getStatus()<1)return CommunityUtil.getJSONString(500,"你的权限不够");
        List<Member> appFrom = memberService.findAppFrom(groupId);
        List<Map<String,Object>>list=new ArrayList<>();
        for (Member index:appFrom){
            Map<String,Object>map=new HashMap<>();
            User user = userService.findUserById(index.getUserId());
            map.put("userId",user.getId());
            map.put("applyName",user.getUsername());
            map.put("applyHeadUrl",user.getHeadurl());
            map.put("relName",user.getRelname());
            map.put("applyTime",index.getCreateTime());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

}
