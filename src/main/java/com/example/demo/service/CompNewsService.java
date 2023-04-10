package com.example.demo.service;

import com.example.demo.dao.CompNewsMapper;
import com.example.demo.eneity.CompNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompNewsService {
    @Autowired
    CompNewsMapper compNewsMapper;
    public List<CompNews> findAll(){
        return compNewsMapper.findAll();
    }

    public List<CompNews> findByStatus(int status){
        return compNewsMapper.findByStatus(status);
    }
    public void updateCount(int id){
        compNewsMapper.updateCount(id);
    }
    public CompNews findById(int id){
        return compNewsMapper.findById(id);
    }
}
