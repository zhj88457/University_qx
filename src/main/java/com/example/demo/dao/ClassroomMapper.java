package com.example.demo.dao;

import com.example.demo.eneity.Classroom;
import com.example.demo.eneity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassroomMapper {
    Classroom findClassByNum(String ClassNum);
    int insertClassroom(String classnum,int status,String term,String ans);
    int updateClassroomAns(String ans,String classnum);
    List<Classroom> findClassrooms();
    List<Classroom>findClassroomByNum(String classnum);
}
