package com.example.demo.dao;

import com.example.demo.eneity.Advert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdvertMapper {
    void addAdvert( Advert advert);
    List<Advert> selectAll();
    List<Advert> selectMy(int userId);

    void putAdvert(String describes,String picture1,String picture2,String picture3,String picture4,int id);

    Advert findById(int id);

    void deleateById(int id);
}
