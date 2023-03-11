package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Course {
    private int id;
    private String courseName;
    private String classroomNum;
    private String startWeek;
    private String endWeek;
    private String week;
    private String lesson;
    private String isEmpty;
}
