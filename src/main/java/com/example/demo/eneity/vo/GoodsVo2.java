package com.example.demo.eneity.vo;

import com.example.demo.eneity.Goods;
import com.example.demo.service.GoodService;
import com.example.demo.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class GoodsVo2 {
    private int sellerId;
    private String name;
    private int id;
    private double price;
    private String detail;
    private String upload_date;
    private int status;
    private String picture;
    private String sellerName;

    public GoodsVo2(Goods goods) {
        this.sellerId = goods.getSellerId();
        this.name = goods.getName();
        this.id = goods.getId();
        this.price = goods.getPrice();
        this.detail = goods.getDetail();
        this.upload_date = goods.getUpload_date();
        this.status = goods.getStatus();
        this.picture = goods.getPicture();
    }
}
