package com.example.demo.dao;

import com.example.demo.eneity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int insertContact(Notice notice);
    int deleteNotice(int noticeId);
    Notice findNoticeById(int noticeId);
    int updateNoticeById(Notice notice);
    List<Notice> findNotice(int groupId);
    List<Notice> findTwoNotice();
    List<Notice> findAll();
}
