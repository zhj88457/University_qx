package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Wanted {
    private int sellerId;
    private int id;
    private int userId;
    private int goodId;
    private String createTime;
}
