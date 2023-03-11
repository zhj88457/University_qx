package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Groups {
    private String headUrl;
    private int id;
    private String name;
    private String detail;
    private String createTime;
    private int status;
    // 0 为待审核社团 1为正式社团 2为请求解散的社团 -1为已解散的社团
    // 对于用户而言 1和2都是正常社团
    private String contact;
    private int userId;
}
