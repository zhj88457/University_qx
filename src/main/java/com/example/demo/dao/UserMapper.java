package com.example.demo.dao;

import com.example.demo.eneity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper  {
    int adduser(User user);
    int countUserByStunum(String stunum);
    User findByStunum(String stunum);
    User findUserById(int userId);
    int addWeChat(User user);
    int countUserByOpenID(String openid);
    User findUserByOpenId(String openid);
    int collectGoods(String stunum ,String collection);
    int resetPwd(int userId,String password);
    int updatePwd(String password,int userId);
    int deleteById(int userId);
    int updateUser(User user);
    List<User> findUsers();
    List<User> findUserBynum(String stunum);
    List<User> findUserByRelName(String relName);
}
