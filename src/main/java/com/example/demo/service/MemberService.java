package com.example.demo.service;

import com.example.demo.dao.MemberMapper;
import com.example.demo.eneity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;
    public Member findMember(int userId,int groupId){
        return memberMapper.selectByUserId(userId, groupId);
    }
    public int insertMember(Member member){
        return memberMapper.insertMember(member);
    }
    public int updateMember(int userId,int groupId,int status){
        return memberMapper.updateMember(userId, groupId, status);
    }
    public int deleteMember(int userId,int groupId){
        return memberMapper.deleteMember(userId, groupId);
    }
    public List findGroupByStatus(int status, int userId){
        return memberMapper.findGroupByStatus(status,userId);
    }
    public int addMember(Member member){
        return memberMapper.addMember(member);
    }
    public Member findById(int groupId,int userId){
        return memberMapper.findById(groupId, userId);
    }
    public List findAll(int groupId){
        return memberMapper.findAll(groupId);
    }
    public List<Member> findAppFrom(int groupId){
        return memberMapper.findAppMembers(groupId);
    }
    public List<Member> findMemberByUserId(int userId){
        return memberMapper.findMemberByUserId(userId);
    }
    public List<Member> findMemberByGoup(int groupId){
        return memberMapper.findMemberByGoup(groupId);
    }
    public int updateById(int mId,int status){
        return memberMapper.updateById(mId, status);
    }
    public Member findMemberById(int mId){
        return memberMapper.findMember(mId);
    }
}
