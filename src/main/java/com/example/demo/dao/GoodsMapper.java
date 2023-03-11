package com.example.demo.dao;

import com.example.demo.eneity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
     int addGoods(Goods goods);
     int updateGoods(Goods goods);
     Goods findById(int id);
     List<Goods> findAll();
     int updateGoodStatus(int goodId);
     int deleateGoods(int goodId);
     List<Goods> obscureSelect(String str);
     int findMaxId();
     int findMinId();
     List<Goods> findMyGoods(int userId);
     List<Integer> findByLabel(String str);
     List<Goods> soldGoods(int userId);
     int updateStatus(int goodId);
     List<Goods> findallGoods();
     String findDate(int goodId);
     int updateAStatus(int goodId);

}
