package com.example.demo.service;

import com.example.demo.dao.ContributionMapper;
import com.example.demo.eneity.Contribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService {
    @Autowired
    ContributionMapper contributionMapper;
    public int insertContribution(Contribution contribution){
        return contributionMapper.addContribution(contribution);
    }
    public List<Contribution> findAll(){
        return contributionMapper.findAll();
    }
    public List<Contribution> myContribution(int userId){
        return contributionMapper.myContribution(userId);
    }
    public int deleteContribution(int cid){
        return contributionMapper.deleteContribution(cid);
    }
}
