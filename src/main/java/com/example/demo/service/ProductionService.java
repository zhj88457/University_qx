package com.example.demo.service;

import com.example.demo.dao.ProductionMapper;
import com.example.demo.eneity.Production;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionService {
    @Autowired
    ProductionMapper productionMapper;
    public Production findProductionById(int pro_id){
        return productionMapper.findProductionById(pro_id);
    }
    public List<Integer> findAllProduction(){
        return productionMapper.findAllProductionIds();
    }
    public int deleteProduction(int pro_id){
        return productionMapper.deleteProduction(pro_id);

    }

    public int uploadFood (Production production){
        return productionMapper.uploadFood(production);
    }
    public List<Production> findAll (){
        return productionMapper.findAll();
    }
    public int updateStatus(int proId){
        return productionMapper.updateStatus(proId);
    }
    public List<Integer> findByLabel(String str){
        return productionMapper.findByLabel(str);
    }
    public List<Integer> findByCantten(String canteen){
        return productionMapper.findByCanteen(canteen);
    }
    public List<Production> findMyProduction(int userId){
        return productionMapper.findMyProductions(userId);
    }
    public int deletePro(int proId){
        return productionMapper.deletePro(proId);
    }
}
