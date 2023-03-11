package com.example.demo.dao;

import com.example.demo.eneity.Contribution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContributionMapper {
     int addContribution(Contribution contribution);
     List<Contribution> findAll();
     List<Contribution> myContribution(int userId);
     int deleteContribution(int cid);
}
