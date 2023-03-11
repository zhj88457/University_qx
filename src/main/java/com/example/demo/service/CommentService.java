package com.example.demo.service;

import com.example.demo.dao.CommentMapper;
import com.example.demo.eneity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    public int addComment(Comment comment){
        return commentMapper.insertComment(comment);
    }
    public List<Comment> selectComment(int entityType, int targetId, int commentType){
        return commentMapper.selectComment(entityType, targetId, commentType);
    }
    public Comment findCommentById(int id){
        return commentMapper.findCommentByid(id);
    }
    public List<Comment> findComment3(int entityType, int targetId, int commentType){
        return commentMapper.findThreeComment(entityType, targetId, commentType);
    }
    public int findMaxId(){
        return commentMapper.findMaxId();
    }
}
