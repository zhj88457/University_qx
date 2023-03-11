package com.example.demo.service;

import com.example.demo.dao.GroupMapper;

import com.example.demo.eneity.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupMapper groupMapper;
    public int createGroup(Groups group){
        return groupMapper.createGroup(group);
    }
    public List<Groups> findAll(){
        return groupMapper.findAll();
    }
    public int countGroupById(int groupId){
        return groupMapper.countGroupById(groupId);
    }
    public int updateStatus(int status,int groupId){
        return groupMapper.updateStatus(status,groupId);
    }
    public List<Groups> findAllByStatus(){
        return groupMapper.findAllByStatus();
    }
    public Groups findGroupById(int groupId){
        return groupMapper.findGroupById(groupId);
    }
    public Groups findGroup(int userId,int groupId){
        return groupMapper.findGroup(userId,groupId);
    }
    public int updateDetail(String detail,int groupId){
        return groupMapper.updateDetail(detail,groupId);
    }
    public List<Groups>findGroupByUserId(int userId){
        return groupMapper.findGroupByUserId(userId);
    }
    public int deleteGroup(int groupId){
        return groupMapper.deleteGroup(groupId);
    }
}
