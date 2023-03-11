package com.example.demo.dao;

import com.example.demo.eneity.Production;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductionMapper {
     Production findProductionById(int pro_id);
     List<Integer> findAllProductionIds();
     int deleteProduction(int pro_id);
     int uploadFood(Production production);
     List<Production> findAll();
     int updateStatus(int proId);
     List<Integer> findByLabel(String str);
     List<Integer>findByCanteen(String canteen);
     List<Production> findMyProductions(int userId);
     int deletePro(int proId);
}
