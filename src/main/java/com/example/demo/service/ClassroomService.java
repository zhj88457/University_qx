package com.example.demo.service;

import com.example.demo.dao.ClassroomMapper;
import com.example.demo.eneity.Classroom;
import com.example.demo.eneity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService  {
    @Autowired
    ClassroomMapper classroomMapper;

    public String sloveExcel(List<Course> list,String term){
        char ans1[]=new char[1000];
        for (int i=0;i<1000;i++)
            ans1[i]='0';
        String ans=new String(ans1);
        for (Course course:list){
            Classroom classroom = classroomMapper.findClassByNum(course.getClassroomNum());
            if (classroom==null){
                Classroom classroom1 = new Classroom();
                classroom1.setAns(ans);
                classroom1.setClassnum(course.getClassroomNum());
                classroom1.setStatus(1);
                classroom1.setTerm(term);
//                System.out.println(classroom1);
                classroomMapper.insertClassroom(course.getClassroomNum(),1,term,ans);
            }
            Classroom room = classroomMapper.findClassByNum(course.getClassroomNum());
            String anss=room.getAns();
            char[] chars = anss.toCharArray();
//            System.out.println(chars);
            int index=2;
            if (Integer.valueOf(course.getIsEmpty())==2)
                index=1;
            for (int i=Integer.valueOf(course.getStartWeek());i<=Integer.valueOf(course.getEndWeek());i+=index){
                chars[(i-1)*35+(Integer.valueOf(course.getWeek())-1)*5+Integer.valueOf(course.getLesson())-1]='1';
            }
            anss=new String(chars);
            classroomMapper.updateClassroomAns(anss,course.getClassroomNum());
        }
        return "ok";
    }
    public Classroom findClassrommByNum(String classnum){
        return classroomMapper.findClassByNum(classnum);
    }
    public List<Classroom> findClassrooms(){
        return classroomMapper.findClassrooms();
    }
    public List<Classroom>findClassroomsByNum(String classnum){
        return classroomMapper.findClassroomByNum(classnum);
    }
}
