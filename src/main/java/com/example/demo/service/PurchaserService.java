package com.example.demo.service;

import com.example.demo.dao.PurchaseMapper;
import com.example.demo.eneity.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaserService {
    @Autowired
    PurchaseMapper purchaseMapper;
    public int addPurchase(Purchase purchase){
        return purchaseMapper.addPurchase(purchase);
    }
    public List<Purchase> findMyPurchase(int userId){
        return purchaseMapper.findMyPurchase(userId);
    }
    public List<Purchase> findMySold(int sellerId){
        return purchaseMapper.findMySold(sellerId);
    }
    public List<Purchase> findAll(){
        return purchaseMapper.findAll();
    }
    public int count(String str){
        return purchaseMapper.count(str);
    }
}
