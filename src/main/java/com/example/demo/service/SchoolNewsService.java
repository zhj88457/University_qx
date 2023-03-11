package com.example.demo.service;

import com.example.demo.dao.SchoolnewsMapper;
import com.example.demo.eneity.SchoolNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolNewsService {
    @Autowired
    SchoolnewsMapper schoolnewsMapper;
    public int addSchoolnews (SchoolNews schoolNews) {
        return schoolnewsMapper.addSchoolnews(schoolNews);
    }
    public List<SchoolNews> findAll(){
        return schoolnewsMapper.findAll();
    }
    public SchoolNews findNewsById(int id){
        return schoolnewsMapper.findNewsById(id);
    }
    public int deleteNews(int nid){
        return schoolnewsMapper.deleteNews(nid);
    }
}
