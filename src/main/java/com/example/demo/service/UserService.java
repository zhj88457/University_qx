package com.example.demo.service;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.dao.UserMapper;
import com.example.demo.eneity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {
    @Autowired
    UserMapper userMapper;
    public int regist(User user){
        String password = user.getPassword();
        user.setSalt(CommunityUtil.generateUUID());
        System.out.println(user.getSalt());
        password= CommunityUtil.md5(password+user.getSalt());
        user.setPassword(password);
        return userMapper.adduser(user);
    }
    public int countUserByStunum(String stunum){
        return userMapper.countUserByStunum(stunum);
    }
    public User findByStunum(String stunum){
        return  userMapper.findByStunum(stunum);
    }
    public int registWeChat(User user){
        return userMapper.addWeChat(user);
    }
    public int countUserByOpenID(String openid){
        return userMapper.countUserByOpenID(openid);
    }
    public User findUserByOpenID(String openid){
        return userMapper.findUserByOpenId(openid);
    }
    public User findUserById(int userId){
        return userMapper.findUserById(userId);
    }
    public int resetPwd(int userId,String password){
        return userMapper.resetPwd(userId,password);
    }

    public int updatePwd(String password,int userId) {
        return userMapper.updatePwd(password,userId);
    }
    public int deleteById(int userId){
        return userMapper.deleteById(userId);
    }
    public int updateUser(User user){
        return userMapper.updateUser(user);
    }
    public List<User> findUsers(){
        return userMapper.findUsers();
    }
    public List<User> findUserBystuNum(String stunum){
        return userMapper.findUserBynum(stunum);
    }
    public List<User> findUserByRelName(String relName){
        return userMapper.findUserByRelName(relName);
    }
}
