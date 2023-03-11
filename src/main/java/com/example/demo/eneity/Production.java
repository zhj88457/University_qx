package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
public class Production {
    private int id;
    private int userId;
    private String name;
    private String detail;
    private String picture;
    private String location;
    private String shopName;
    private double price;
    private String uploadTime;
    private int status;
}
