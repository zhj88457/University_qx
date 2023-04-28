package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Advert {
    private int id;
    private String describes;
    private String createTime;
    private String picture1;
    private String picture2;
    private String picture3;
    private String picture4;
    private int userId;

}
