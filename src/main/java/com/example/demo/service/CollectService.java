package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectService {
    @Autowired
    UserMapper userMapper;
    public int collectGoods(String stunum,String collection){
        return userMapper.collectGoods(stunum,collection);
    }
}
