package com.example.demo.dao;

import com.example.demo.eneity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectComment(int entityType, int targetId, int commentType);
    int insertComment(Comment comment);
    Comment findCommentByid(int id);
    List<Comment> findThreeComment(int entityType, int targetId, int commentType );
    int findMaxId();
}
