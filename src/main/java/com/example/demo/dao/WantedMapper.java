package com.example.demo.dao;

import com.example.demo.eneity.Wanted;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WantedMapper {
    int addWanted(Wanted wanted);
    List getWantedList(int userId);
    Wanted findWantedByGoodId(int GoodId);
    int countWanted(int goodId);
    Wanted findWantedByUserGoodId(int userId,int goodId);
    List<Wanted> findWantedByUserId(int userId);
    int deleteWanted(int userId,int goodId);
}
