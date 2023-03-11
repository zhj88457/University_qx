package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;
    public void like(int entityType,int entityId,int userId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey="like:"+entityType+":"+entityId;
                boolean isMember=operations.opsForSet().isMember(entityLikeKey,userId);
                operations.multi();
                if (isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                }else{
                    operations.opsForSet().add(entityLikeKey,userId);
                }
                return operations.exec();
            }
        });
    }

    //查询某人对某实体的点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = "like:"+entityType+":"+entityId;
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
//    public long findProductionLikeCount(int entityType,int entityId){
//        String entityLikeKey = "like:"+entityType+":"+entityId;
//    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = "like:"+entityType+":"+entityId;
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
