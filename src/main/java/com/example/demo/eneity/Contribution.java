package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Contribution {
    private int id;
    private String picture1;
    private String picture2;
    private String picture3;
    private String submitTime;
    private int userId;
    private String content;
    private int status;
}