package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompNews {
    private int id;
    private String title;
    private  int status;
    private  String content;
    private String picture1;
    private String picture2;
    private String picture3;
    private String createTime;
    private int count;
}
