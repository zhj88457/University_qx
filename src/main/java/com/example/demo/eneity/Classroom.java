package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Classroom {
    private int id;
    private String classnum;
    private int status;
    private String term;
    private String ans;
}
