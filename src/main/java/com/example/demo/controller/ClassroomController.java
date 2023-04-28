package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Classroom;
import com.example.demo.service.ClassroomService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ClassroomController {
    @Autowired
    ClassroomService classroomService;

    @GetMapping("/findTime")//根据教室号查询所有教室的空闲情况
    public String findTime(int week,int day,String classnum){
//        List<Classroom> classroomList = classroomService.findClassrooms();
        List<Classroom> classrommByNum = classroomService.findClassroomsByNum(classnum);
//        System.out.println(classrommByNum);
        List<Classroom> list=new ArrayList<>();
        if ((classnum.equals("10")||classnum.equals("11"))){
            for (Classroom index:classrommByNum){
                if (index.getClassnum().length()>4){
                    list.add(index);
                }
            }
        }else {
            list=classrommByNum;
        }
//        System.out.println(list);
        List<Map<String,Object>>mapList=new ArrayList<>();
        Map<String,Object>resultMap=new HashMap<>();
        for (Classroom index:list){
            String ans = index.getAns();
            char ans1[]=ans.toCharArray();
//            List<String> statusList=new ArrayList<>();
            StringBuilder result=new StringBuilder();
            for (int i=(week-1)*35+(day-1)*5;i<35*(week-1)+day*5;i++){
                result.append(ans1[i]);
            }
            Map<String,Object>map=new HashMap<>();
            map.put("classnum",index.getClassnum());
            map.put("status",result);
            mapList.add(map);
        }
        Collections.sort(mapList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("classnum").toString().compareTo(o2.get("classnum").toString())>0)
                return 1;
                else if (o1.get("classnum").toString().compareTo(o2.get("classnum").toString())<0) return -1;
                 return 0;
            }
        });
        resultMap.put("result",mapList);
        return CommunityUtil.getJSONString(200,"查询成功",resultMap);
    }
    @GetMapping("findClass")
    public String findClass(String week,int lesson,int day){
        List<Classroom> classrooms = classroomService.findClassrooms();
        List<String>classroom=new ArrayList<>();
        for (int i = 0; i < classrooms.size(); i++) {
            if (classrooms.get(i).getAns().toCharArray()[(Integer.valueOf(week)-1)*35+(day-1)*5+lesson-1]=='0'){
                classroom.add(classrooms.get(i).getClassnum());
            }
        }
        Map<String,Object>map=new HashMap<>();
        map.put("classroom",classroom);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("computeDate")
    public String computeDate() throws ParseException {
        String startDate="2023-02-21";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sd.parse(startDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sd.parse(sd.format(new Date())));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        Map<String,Object>map=new HashMap<>();
       int result=Integer.parseInt(String.valueOf(between_days));
       int week=result/7+1;
       int days=result%7+1;
        map.put("week",week);
        map.put("days",days);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
