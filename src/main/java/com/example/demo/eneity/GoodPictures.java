package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoodPictures {
    private int id;
    private int goodId;
    private String pictureUrl;
    private int userId;
}
