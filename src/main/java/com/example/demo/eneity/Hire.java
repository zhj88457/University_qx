package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Hire {
    private int id;
    private String name;
    private String createTime;
    private int sellerId;
    private String picture;
}
