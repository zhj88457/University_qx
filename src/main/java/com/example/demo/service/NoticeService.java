package com.example.demo.service;

import com.example.demo.dao.NoticeMapper;
import com.example.demo.eneity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    public int addNotice(Notice notice){
        return noticeMapper.insertContact(notice);
    }
    public int deleteNotice(int noticeId){
        return noticeMapper.deleteNotice(noticeId);
    }
    public Notice findNotcieById(int noticeId){
        return noticeMapper.findNoticeById(noticeId);
    }
    public int updateNotice(Notice notice){
        return noticeMapper.updateNoticeById(notice);
    }
    public List<Notice> findNotices(int groupId){
        return noticeMapper.findNotice(groupId);
    }
    public List<Notice> findTwoNotice(){
        return noticeMapper.findTwoNotice();
    }
    public List<Notice>findAll(){
        return noticeMapper.findAll();
    }

}
