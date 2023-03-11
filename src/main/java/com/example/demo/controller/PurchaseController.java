package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.Goods;
import com.example.demo.eneity.Purchase;
import com.example.demo.service.GoodService;
import com.example.demo.service.PurchaserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PurchaseController {
    @Autowired
    PurchaserService purchaserService;
    @Autowired
    GoodService goodService;

    @GetMapping("myPurchase")
    public String myPurchase(int userId){
        List<Purchase> myPurchase = purchaserService.findMyPurchase(userId);
        List<Map<String,Object>> list=new ArrayList<>();
        for (Purchase index:myPurchase){
            Map<String,Object> map=new HashMap<>();
            Goods good = goodService.findById(index.getGoodId());
            if (good==null)continue;
            map.put("goodName",good.getName());
            map.put("goodPrice",good.getPrice());
            map.put("goodUrl",good.getPicture());
            map.put("goodId",good.getId());
            map.put("purchaseTime",index.getPurchaseTime());
            map.put("goodDetail",good.getDetail());
            list.add(map);
        }
        Map<String,Object>map=new HashMap<>();
        map.put("result",list);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
}
