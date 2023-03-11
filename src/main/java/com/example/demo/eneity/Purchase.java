package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Purchase {
    private int id;
    private int sellerId;
    private int buyerId;
    private int goodId;
    private String purchaseTime;
}
