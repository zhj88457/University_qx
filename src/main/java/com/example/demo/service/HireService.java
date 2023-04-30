package com.example.demo.service;

import com.example.demo.dao.HireMapper;
import com.example.demo.eneity.Hire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HireService {
    @Autowired
    HireMapper hireMapper;

    public List<Hire> findAll(){
        return hireMapper.findAll();
    }

    public void addOne(Hire hire){
        hireMapper.addOne(hire);
    }
    public List<Hire> findAMyHire(int userId){
        return hireMapper.findMyHire(userId);
    }

    public void putHire(String name,String picture,int id){
        hireMapper.putHire(name, picture, id);
    }

    public Hire findById(int id){
        return hireMapper.findById(id);
    }

    public void deleteById(int id){
        hireMapper.deleteById(id);;
    }

}
