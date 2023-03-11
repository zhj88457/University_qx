package com.example.demo.eneity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment {
    private int id;
    private int fromId;
    private String content;
    private int targetId;
    private int commentType;
    private String commentTime;
    private int entityType;
}
