package com.example.demo.eneity.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GoodsVo {
    private int sellerId;
    private String name;
    private int id;
    private double price;
    private String detail;
    private String upload_date;
    private int status;
    private String picture;
    private Integer userId;
    private String cover;
    private List<String> pictures;
}
