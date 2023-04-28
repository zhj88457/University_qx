package com.example.demo.service;

import com.example.demo.dao.AdvertMapper;
import com.example.demo.eneity.Advert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertService{
    @Autowired
    AdvertMapper advertMapper;
    public void addAdvert(Advert advert){
        advertMapper.addAdvert(advert);
    }

    public List<Advert> selectAll(){
        return advertMapper.selectAll();
    }

    public List<Advert>selectMy(int userId){
        return advertMapper.selectMy(userId);
    }
    public void putAdvert(String describes,String picture1,String picture2,String picture3,String picture4,int id){
        advertMapper.putAdvert(describes, picture1, picture2, picture3, picture4, id);
    }
    public Advert findById(int id){
        return advertMapper.findById(id);
    }
    public void deleteById(int id){
        advertMapper.deleateById(id);
    }
}
