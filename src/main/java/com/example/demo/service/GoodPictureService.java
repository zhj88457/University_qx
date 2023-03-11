package com.example.demo.service;

import com.example.demo.dao.GoodPicturesMapper;
import com.example.demo.eneity.GoodPictures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodPictureService {
    @Autowired
    GoodPicturesMapper goodPicturesMapper;
    public int addgoodPictures(GoodPictures goodPictures){
        return goodPicturesMapper.addGoodPictures(goodPictures);
    }
    public List<GoodPictures> findGoodPicture(int goodId){
        return goodPicturesMapper.findGoodPictures(goodId);
    }
    public int deleteGoodPicture(int goodId){
        return goodPicturesMapper.deleteGoodPicture(goodId);
    }
}
