package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Member;
import com.example.demo.eneity.Message;
import com.example.demo.eneity.Notice;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class NoticeController {
    @Autowired
    NoticeService noticeService;
    @Autowired
    MemberService memberService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    GroupService groupService;
    @Autowired
    MessageService messageService;

    @PostMapping("/notice")
    public String addNotice(Notice notice,  @RequestParam List<String> pictures){
        Member member = memberService.findMember(notice.getUserId(), notice.getGroupId());
        if (member==null||member.getStatus()<1){
            return CommunityUtil.getJSONString(500,"您的权限不够");
        }
        if (notice.getContext()==null||notice.getContext().length()==0){
            return CommunityUtil.getJSONString(500,"公告内容不能为空");
        }
        if (notice.getTitle()==null||notice.getTitle().length()==0) return CommunityUtil.getJSONString(500,"标题不能为空");
        notice.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (pictures!=null){
            if (pictures.size()>0)
            notice.setPicture1(pictures.get(0));
            if (pictures.size()>1)
            notice.setPicture2(pictures.get(1));
            if (pictures.size()>2)
            notice.setPicture3(pictures.get(2));
        }
        noticeService.addNotice(notice);
        List<Member> all = memberService.findMemberByGoup(notice.getGroupId());
        System.out.println(all);
        Message message = new Message();
        message.setFromId(50);
        message.setMsgType(1);
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        message.setStatus(0);
        for (Member index:all){
            message.setToId(index.getUserId());
            message.setContent("您加入的"+groupService.findGroupById(index.getGroupId()).getName()+"有了新的公告，请前往查看");
            messageService.addMessage(message);
        }
        return CommunityUtil.getJSONString(200,"发送成功");
    }
    @DeleteMapping("/notice")
    public String deleteNotice(int noticeId,int userId,int groupId){
        Member member = memberService.findMember(userId, groupId);
        if (member==null||member.getStatus()<1){
            return CommunityUtil.getJSONString(500,"您的权限不够");
        }
        Notice notcieById = noticeService.findNotcieById(noticeId);
        if (notcieById==null){
            return CommunityUtil.getJSONString(500,"该公告不存在");
        }
        noticeService.deleteNotice(noticeId);
        return CommunityUtil.getJSONString(200,"删除成功");
    }
    @PutMapping("/notice")
    public String updateNotice(Notice notice,@RequestParam List<String> pictures){
        if (pictures!=null){
            if (pictures.size()>0)
                notice.setPicture1(pictures.get(0));
            if (pictures.size()>1)
                notice.setPicture2(pictures.get(1));
            if (pictures.size()>2)
                notice.setPicture3(pictures.get(2));
        }
        Notice notcieById = noticeService.findNotcieById(notice.getId());
        if (notcieById==null){
            return CommunityUtil.getJSONString(500,"该公告不存在");
        }
        Member member = memberService.findMember(notice.getUserId(), notice.getGroupId());
        if (member==null||member.getStatus()<1){
            return CommunityUtil.getJSONString(500,"您的权限不够");
        }
        if (notice.getContext()==null||notice.getContext().length()==0) return CommunityUtil.getJSONString(500,"公告内容不得为空");
        if (notice.getTitle()==null||notice.getTitle().length()==0) return CommunityUtil.getJSONString(500,"标题不能为空");
        noticeService.updateNotice(notice);
        return CommunityUtil.getJSONString(200,"修改成功");
    }
    @GetMapping("/notice")
    public String selectNotice(int groupId,int userId){
        List<Notice> notices = noticeService.findNotices(groupId);
        List<Map<String,Object>>list=new ArrayList<>();
        for (Notice index:notices){
            Map<String,Object>map=new HashMap<>();
            List<String>list1=new ArrayList<>();
            map.put("writerHeadUrl",userService.findUserById(index.getUserId()).getHeadurl());
            map.put("writerName",userService.findUserById(index.getUserId()).getUsername());
            map.put("context",index.getContext());
            map.put("sendTime",index.getCreateTime());
            map.put("title",index.getTitle());
            map.put("id",index.getId());
            if (index.getPicture1()!=null)
            list1.add(index.getPicture1());
            if (index.getPicture2()!=null)
            list1.add(index.getPicture2());
            if (index.getPicture3()!=null)
            list1.add(index.getPicture3());
            map.put("pictures",list1);
            map.put("commentSize",commentService.selectComment(1,index.getId(),3).size());
            list.add(map);
        }
        HashMap<String, Object> map = new HashMap<>();
        Member member = memberService.findMember(groupId, userId);
        if (member==null){
            map.put("userStatus",-1);
        }else {
            map.put("userStatus",member.getStatus());
        }
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("/indexNotice")
    public String indexNotice(){
        List<Notice> all = noticeService.findAll();
        List<Map<String,Object>>list=new ArrayList<>();
        for (Notice index:all){
            Map<String,Object>map=new HashMap<>();
            List<String>list1=new ArrayList<>();
            map.put("groupName",groupService.findGroupById(index.getGroupId()).getName());
            map.put("writerHeadUrl",userService.findUserById(index.getUserId()).getHeadurl());
            map.put("writerName",userService.findUserById(index.getUserId()).getUsername());
            map.put("context",index.getContext());
            map.put("sendTime",index.getCreateTime());
            map.put("title",index.getTitle());
            map.put("noticeId",index.getId());
            map.put("groupId",index.getGroupId());
            if (index.getPicture1()!=null)
                list1.add(index.getPicture1());
            if (index.getPicture2()!=null)
                list1.add(index.getPicture2());
            if (index.getPicture3()!=null)
                list1.add(index.getPicture3());
            map.put("pictures",list1);
            map.put("commentSize",commentService.selectComment(1,index.getId(),3).size());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
