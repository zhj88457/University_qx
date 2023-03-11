package com.example.demo.service;

import com.example.demo.dao.GoodsMapper;
import com.example.demo.eneity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    GoodsMapper goodsMapper;
    public int insertGoods(Goods goods){
        return  goodsMapper.addGoods(goods);
    }
    public int updateGoods(Goods goods){
        return goodsMapper.updateGoods(goods);
    }
    public Goods  findById(int id){
        return  goodsMapper.findById(id);
    }
    public List<Goods> findAll(){
        return goodsMapper.findAll();
    }
    public int updateGoodStatus(int goodId){
        return goodsMapper.updateGoodStatus(goodId);
    }
    public int deleteGoods(int goodId){
        return goodsMapper.deleateGoods(goodId);
    }
    public List<Goods> obscureSelect(String str){return goodsMapper.obscureSelect(str);}
    public int findMaxId(){
        return goodsMapper.findMaxId();

    }
    public int findMinId(){
        return goodsMapper.findMinId();
    }
    public List<Goods> findMyGood(int userId){
        return goodsMapper.findMyGoods(userId);
    }
    public List<Integer> findByLabel(String str){
        return goodsMapper.findByLabel(str);
    }
    public List<Goods> soldGoods(int userId){
        return goodsMapper.soldGoods(userId);
    }
    public int updateStatus(int goodId){
        return goodsMapper.updateStatus(goodId);
    }
    public  List<Goods> findallGoods(){
        return goodsMapper.findallGoods();
    }
    public String findDate(int goodId){
        return goodsMapper.findDate(goodId);
    }
    public int updateAstatus(int goodId){
        return goodsMapper.updateAStatus(goodId);
    }
}
