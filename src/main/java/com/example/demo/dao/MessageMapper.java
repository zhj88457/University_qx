package com.example.demo.dao;

import com.example.demo.eneity.Message;
import com.example.demo.eneity.MsgStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    int countUnreadConversations(int userId,int msgType);
    int insertConversation(Message message);
    List<Message> findSystemConversations(int userId);
    List<Message> findMessageByToId(int toId,int msgType);
    int updateMessageStatus(int userId,int msgType);
    List<Message> findMessage (int from_id,int to_id);
    List unReadmessage(int from_id,int to_id);
    List unReadmessage(int from_id, int to_id, int msg_type);
    List<Message> unReadMsg(int userId);
    int updateMessage(int status,int userId);
    List getMessage(int userId);
    List getFromMessage(int userId);
    int countUnreadMessage(int userId);
    MsgStatus selectMsg(int fromId,int toId);
    int updateMsg(int id,int status);
    int addMsg(MsgStatus msgStatus);
    int unmessage(int fromId,int userId);
    Message findPurchaseMessage(int fromId,int toId);
    List<Message> findunredMessage(int userId);
    List<Message> findMessageByTypeTo(int toId,int msgType);
    int deleteMessage(int messageId);
}
