package com.example.demo.service;

import com.example.demo.dao.MessageMapper;
import com.example.demo.eneity.Message;
import com.example.demo.eneity.MsgStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;
    public int countUnreadMessage(int userId,int msgType){
        return messageMapper.countUnreadConversations(userId,msgType);
    }
    public int addMessage(Message message){
        return messageMapper.insertConversation(message);
    }
    public List<Message> findSystemMessage(int userId){
        return messageMapper.findSystemConversations(userId);
    }
    public List<Message> findSystemByToId(int toId,int msgType){
        return messageMapper.findMessageByToId(toId,msgType);
    }
    public int updateMessageStatus(int userId,int msgType){
        return messageMapper.updateMessageStatus(userId,msgType);
    }
    public List<Message> findMessage (int from_id,int to_id){
        return messageMapper.findMessage(from_id,to_id);
    }
    public List unReadmessage(int from_id,int to_id,int msg_type){
        return messageMapper.unReadmessage(from_id,to_id,msg_type);
    }
    public  List<Message> unReadMsg(int userId) {
        return messageMapper.unReadMsg(userId);
    }
    public int updateMessage(int status,int userId){
        return messageMapper.updateMessage(status,userId);
    }
    public List getMessage(int userId){
        return messageMapper.getMessage(userId);
    }
    public List getFromMessage(int userId){
        return messageMapper.getFromMessage(userId);
    }
    public int countUnreadMessage(int userId) {
        return messageMapper.countUnreadMessage(userId);
    }
    public MsgStatus findMsgStatus(int fromId,int toId){
        return messageMapper.selectMsg(fromId, toId);
    }
    public int updateMsg(int id,int status){
        return messageMapper.updateMsg(id, status);
    }
    public int addMsg(MsgStatus msgStatus){
        return messageMapper.addMsg(msgStatus);
    }
    public int unmessage(int fromId,int userId){
        return messageMapper.unmessage(fromId,userId);
    }
    public Message findPurches(int fromId,int Toid){
        return messageMapper.findPurchaseMessage(fromId, Toid);
    }
    public List<Message> findUnreadMessage(int userId){
        return messageMapper.findunredMessage(userId);
    }
    public List<Message> findMessageByType(int toId,int msgType){
       return messageMapper.findMessageByTypeTo(toId, msgType);
    }
    public int deleteMessage(int messageId){
        return messageMapper.deleteMessage(messageId);
    }

}
