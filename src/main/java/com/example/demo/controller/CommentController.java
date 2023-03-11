package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.SensitiveFilter;
import com.example.demo.eneity.Comment;
import com.example.demo.eneity.Event;
import com.example.demo.eneity.User;
import com.example.demo.event.EventProducer;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    // 需要参数 userId评论人ID，content评论内容,targetId 被评论目标的Id，commentType评论位置(二手交易为1，表白墙为2,3为社团)，entityType 为被评论目标的类型，1为某帖子，2为某评论
    @PostMapping("/comment")
    public String postComment(Comment comment,int userId){
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        comment.setFromId(userId);
        comment.setCommentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        int i = commentService.addComment(comment);
        User user = userService.findUserById(userId);
        if (comment.getEntityType()==2){
            Event event = new Event();
            event.setEntityType(1);
            event.setTopic("commented");
            event.setEntityId(comment.getTargetId());
            event.setEntityUserId(commentService.findCommentById(comment.getTargetId()).getFromId());
            event.setUserId(userId);
            event.setData("type","comment");
            event.setData("content",comment.getContent());
//            event.setData("toId",);
            event.setData("name",user.getUsername());
            event.setData("headUrl",user.getHeadurl());
            eventProducer.fireEvent(event);
        }
        return CommunityUtil.getJSONString(200,"评论成功");
    }
    //targetId 被评论目标的Id,entityType 为被评论目标的类型，1为某帖子，2为某评论,commentType评论位置(二手交易为1，表白墙为2，3为社团,4为食堂)
    @GetMapping("/comment")
    public String getComment(int entityType, int targetId, int commentType){
        List<Comment> comments = commentService.selectComment(entityType, targetId, commentType);
        if (comments==null||comments.size()<1)
            return CommunityUtil.getJSONString(250,"没有评论或者回复");
        List<Map<String,Object>> list=new ArrayList<>();
        for (Comment comment:comments){
            HashMap<String,Object>map=new HashMap<>();
            if (comment==null) continue;
            if (entityType==1){
                List<Comment> twiceComment = commentService.selectComment(2, comment.getId(), commentType);
                map.put("commentCount",twiceComment.size());
            }
            map.put("content",comment.getContent());
            map.put("Time",comment.getCommentTime());
            map.put("userName",userService.findUserById(comment.getFromId()).getUsername());
            map.put("userHeadUrl",userService.findUserById(comment.getFromId()).getHeadurl());
            map.put("commentId",comment.getId());
            list.add(map);
        }
        HashMap<String, Object> map = new HashMap<>();
//        map.put("commentCount",list.size());
        map.put("comments",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
