package com.example.demo.dao;

import com.example.demo.eneity.GoodPictures;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodPicturesMapper {
    int addGoodPictures(GoodPictures goodPictures);
    List<GoodPictures> findGoodPictures(int goodId);
    int deleteGoodPicture(int goodId);
}
