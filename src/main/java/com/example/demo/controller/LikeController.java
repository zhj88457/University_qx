package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.HostHolder;
import com.example.demo.eneity.Event;
import com.example.demo.eneity.Goods;
import com.example.demo.eneity.Production;
import com.example.demo.eneity.User;
import com.example.demo.event.EventProducer;
import com.example.demo.service.GoodService;
import com.example.demo.service.LikeService;
import com.example.demo.service.ProductionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    GoodService goodService;
    @Autowired
    UserService userService;
    @Autowired
    ProductionService productionService;

    @Autowired
    EventProducer eventProducer;
    @GetMapping(path = "/like")
    //食堂点赞 type 2
    public String like(int entityType,int entityId,int userId){
        likeService.like(entityType,entityId,userId);
        User user = userService.findUserById(userId);

        // 数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(userId, entityType, entityId);
        // 返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        if (likeStatus==1){
            Event event = new Event();
            event.setTopic("liked");
            event.setEntityId(entityId);
            event.setEntityType(entityType);
            event.setUserId(userId);
            event.setData("headUrl",user.getHeadurl());
            event.setData("name",user.getUsername());
            event.setData("type","liked");
            if (entityType==1){
                Goods goods = goodService.findById(entityId);
                event.setEntityUserId(goods.getSellerId());
            }else if (entityType==2){
                Production production = productionService.findProductionById(entityId);
                event.setData("foodUrl",production.getPicture());
                event.setEntityUserId(production.getUserId());
            }
            //未完待续 entityUserId要根据 entityType的entityId查到作者/卖家的id
            eventProducer.fireEvent(event);
            return CommunityUtil.getJSONString(200,"点赞成功",map);
        }
        return CommunityUtil.getJSONString(200,"取消成功");
    }
    @GetMapping("/liked")
    public String liked(int entityType,int entityId,int userId){
        // 数量
        Map<String, Object> map = new HashMap<>();
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        map.put("likeCount", likeCount);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(userId, entityType, entityId);
        map.put("likeStatus", likeStatus);
        return CommunityUtil.getJSONString(200,null,map);
    }
}
