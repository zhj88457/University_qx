package com.example.demo.event;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.eneity.Event;
import com.example.demo.eneity.Message;
import com.example.demo.service.MessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer {
    @Autowired
    MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics ={"liked","commented","purchase"})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record==null||record.value()==null){
            logger.error("消息内容为空！");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if (event==null){
            logger.error("消息格式发生错误");
            return;
        }
        Message message = new Message();
        message.setFromId(event.getUserId());
        if (event.getData().get("type").equals("liked"))
            message.setMsgType(2);
        else if (event.getData().get("type").equals("comment"))
            message.setMsgType(3);
        else {
            message.setMsgType(4);
        }
        message.setToId(event.getEntityUserId());
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
