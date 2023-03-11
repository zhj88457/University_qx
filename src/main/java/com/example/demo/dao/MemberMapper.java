package com.example.demo.dao;

import com.example.demo.eneity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
     Member selectByUserId(int userId,int groupId);
     int insertMember(Member member);
     int updateMember(int userId,int groupId,int status);
     int deleteMember(int userId,int groupId);
     List findGroupByStatus(int status, int userId);
     int addMember(Member member);
     Member findById(int groupId,int userId);
     List findAll(int groupId);
     List<Member> findAppMembers(int groupId);
     List<Member> findMemberByUserId(int userId);
     List<Member> findMemberByGoup(int groupId);
     int updateById(int mId,int status);
     Member findMember(int mId);
}
