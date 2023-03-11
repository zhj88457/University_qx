package com.example.demo.service;

import com.example.demo.dao.BuildingMapper;
import com.example.demo.eneity.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {
    @Autowired
    BuildingMapper buildingMapper;
    public List<Building> findAll(){
        return buildingMapper.findAll();
    }
    public int deleteById(int bid){
        return buildingMapper.deleteById(bid);
    }
    public int addBuilding(Building building){
        return buildingMapper.addBuildings(building);
    }
}
