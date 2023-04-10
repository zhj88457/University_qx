package com.example.demo.dao;

import com.example.demo.eneity.CompNews;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompNewsMapper {
    List<CompNews> findAll();
    List<CompNews> findByStatus(int status);
    CompNews findById(int id);
    void updateCount(int id);
}
