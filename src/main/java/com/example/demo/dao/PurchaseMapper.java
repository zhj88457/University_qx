package com.example.demo.dao;

import com.example.demo.eneity.Purchase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    int addPurchase(Purchase purchase);
    List<Purchase> findMyPurchase(int userId);
    List<Purchase> findMySold(int sellerId);
    List<Purchase> findAll();
    int count(String str);
}
