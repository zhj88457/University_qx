package com.example.demo.dao;

import com.example.demo.eneity.SchoolNews;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchoolnewsMapper {
    int addSchoolnews(SchoolNews schoolNews);
    List<SchoolNews> findAll();
    SchoolNews findNewsById(int id);
    int deleteNews(int nid);
}
