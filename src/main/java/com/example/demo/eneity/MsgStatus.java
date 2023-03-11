package com.example.demo.eneity;

import lombok.Data;

@Data
public class MsgStatus {
    private int id;
    private int fromId;
    private int toId;
    private int status;
}
