package com.example.demo.eneity.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberVo {
    private  int id;
    private String username;
    private String headUrl;
    private int status;
    private  String createTime;
}
