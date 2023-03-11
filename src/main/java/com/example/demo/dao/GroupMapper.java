package com.example.demo.dao;

import com.example.demo.eneity.Groups;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    int createGroup(Groups group);
    List<Groups> findAll();
    List<Groups> findAllByStatus();
    int countGroupById(int groupId);
    int updateStatus(int status, int groupId);
    Groups findGroupById(int groupId);
    Groups findGroup(int userId, int groupId);
    int updateDetail(String detail, int groupId);
    List<Groups> findGroupByUserId(int userId);
    int deleteGroup(int groupId);
}
