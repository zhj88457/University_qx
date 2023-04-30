package com.example.demo.dao;

import com.example.demo.eneity.Hire;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HireMapper {
    List<Hire> findAll();
    void addOne(Hire hire);
    List<Hire> findMyHire(int userId);

    void putHire(String name,String picture,int id);

    Hire findById(int id);
    void deleteById(int id);
}
