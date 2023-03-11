package com.example.demo.service;

import com.example.demo.dao.WantedMapper;
import com.example.demo.eneity.Wanted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WantedService {
    @Autowired
    WantedMapper wantedMapper;
    public int addWanted(Wanted wanted){
        return wantedMapper.addWanted(wanted);
    }
    public List getWantedList(int userId){
        return wantedMapper.getWantedList(userId);
    }
    public Wanted findWantedById(int goodId){
        return wantedMapper.findWantedByGoodId(goodId);
    }
    public int countWanted(int goodId){
        return wantedMapper.countWanted(goodId);
    }
    public Wanted findWantByUserGoodId(int userId,int goodId){
        return wantedMapper.findWantedByUserGoodId(userId, goodId);
    }
    public List<Wanted>findWantedByUserId(int userId){
        return wantedMapper.findWantedByUserId(userId);
    }
    public int deleteWant(int userId,int goodId){
        return wantedMapper.deleteWanted(userId, goodId);
    }
}
