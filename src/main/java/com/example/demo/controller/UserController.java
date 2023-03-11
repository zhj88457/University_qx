package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.HostHolder;
import com.example.demo.Util.PictureLoadUtil;
import com.example.demo.eneity.LoginTicket;
import com.example.demo.eneity.Member;
import com.example.demo.eneity.User;
import com.example.demo.service.MemberService;
import com.example.demo.service.UserService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MemberService memberService;

    @Value("${cos.accessKey}")
    private String accessKey;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.bucket}")
    private String bucket;
    @Value("${cos.bucketName}")
    private String bucketName;
    @Value("${cos.path}")
    private String path;


    @Autowired
    PictureLoadUtil pictureLoadUtil;
    @PostMapping(path = "/regist")
    public String regist(User user, MultipartFile file){
        int count = userService.countUserByStunum(user.getStunum());
        if (count==0){
            String path = pictureLoadUtil.loadPicture(file);
            user.setHeadurl(path);
            user.setStatus(1);
            user.setSchool("郑州经贸学院");
            userService.regist(user);
            return CommunityUtil.getJSONString(200,"注册成功");
        }
        else {
            return CommunityUtil.getJSONString(500,"注册失败，该用户已存在");
        }
    }
    @PostMapping(path = "/login")
    public String login(String stunum, String password){
        System.out.println(stunum);
        System.out.println(password);
            User user =null;
            user = userService.findByStunum(stunum);
            if (user==null||user.getStatus()==0){
                return CommunityUtil.getJSONString(500,"用户不存在");
            }
            String salt = user.getSalt();
            password= CommunityUtil.md5(password+ salt);
            if (!(user.getPassword()).equals(password)) {
                        return CommunityUtil.getJSONString(500,"密码不正确");
                    } else {
               HashMap<String,Object> map=new HashMap<>();
               map.put("user",user);
                return CommunityUtil.getJSONString(200,"登录成功",map);
                    }
    }
    @GetMapping("/User")
    public String getUser(){
        User user = hostHolder.getUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("user",user);
        return CommunityUtil.getJSONString(200,"获取登录对象成功",map);
    }
    @PutMapping( "/resetPwd")
    public String resrPwd(int userId){
        User user = userService.findUserById(userId);
        if (user!=null){
            String salt = user.getSalt();
            String password = "123456";
            password = CommunityUtil.md5(password+ salt);
            userService.resetPwd(userId,password);
            return CommunityUtil.getJSONString(200,"重置成功");
        }
        return CommunityUtil.getJSONString(500,"该用户不存在");
    }
    @PutMapping("/updatePwd")
    public String updatePwd(String stuNum,String relName,String password){
        User user = userService.findByStunum(stuNum);
        if (user!=null){
            if (user.getRelname().equals(relName)){
                password = CommunityUtil.md5(password+ user.getSalt());
                userService.updatePwd(password,user.getId());
                return CommunityUtil.getJSONString(200,"修改成功");
            }else {
                return CommunityUtil.getJSONString(500,"用户信息错误");
            }
        }else {
            return CommunityUtil.getJSONString(500,"该用户不存在");
        }
    }
    //修改个人信息
    @PutMapping(path = "/updateUser")
    public String update(User user,int userId,String headPicture){
        String path=headPicture;
        System.out.println(user);
        user.setId(userId);
        user.setHeadurl(path);
        userService.updateUser(user);
        return CommunityUtil.getJSONString(200,"修改成功");
    }
    @GetMapping("/userDetail")
    public String getUser(int userId,int groupId){
        User user = userService.findUserById(userId);
        Map<String,Object> map=new HashMap<>();
        if(groupId!=0){
            Member member = memberService.findMember(userId, groupId);
            map.put("status",member.getStatus());
        }
        map.put("username",user.getUsername());
        map.put("relName",user.getRelname());
        map.put("tel",user.getTel());
        map.put("stuNum",user.getStunum());
        map.put("headUrl",user.getHeadurl());
        map.put("school",user.getSchool());
        Map<String,Object>map1=new HashMap<>();
        map1.put("result",map);
        return CommunityUtil.getJSONString(200,"查询成功",map1);
    }
}


