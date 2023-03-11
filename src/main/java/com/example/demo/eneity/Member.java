package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Member {
    private int id;
    private int groupId;
    private int userId;
    private int status;
    // status -1为已退出用户 0为普通用户 1为管理员 2为创立者 -2为请求加入用户
    private String createTime;
}
