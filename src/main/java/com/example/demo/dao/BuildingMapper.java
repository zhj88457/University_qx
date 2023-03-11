package com.example.demo.dao;

import com.example.demo.eneity.Building;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuildingMapper {
    List<Building> findAll();
    int deleteById(int bId);
    int addBuildings(Building building);
}
