package com.example.demo.imSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Util.ApiReturnUtil;
import com.example.demo.eneity.Message;
import com.example.demo.eneity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server extends WebSocketServer {
    //构造方法
    public Server(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }
    private MessageService messageService = (MessageService) ApplicationHelper.getBean("messageService");
    private UserService userService = (UserService) ApplicationHelper.getBean("userService");
    //记录当前在线人数
    private int onLineCount = 0;
    //    private static MessageService messageService;
    //在线清单
    Map<String,WebSocket> onLineList = new HashMap<String,WebSocket>();
    //获取MessageService和UserService对象
    private String userId;
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        //获取参数中当前用户id
        String param = clientHandshake.getResourceDescriptor();
        userId = param.substring(1, param.length());
        System.out.println("userId: " + userId);
        //在线人数加一
        addOnLineCount();
        //将当前用户id和WebSocket对象加入在线清单
        onLineList.put(userId, webSocket);
        //检查自己是否有未接收的消息
        List<Message> messageList = messageService.unReadMsg(Integer.parseInt(userId));
        if (messageList != null) {
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                User user = userService.findUserById(message.getFromId());
                JSONObject jsonObjectMessage = new JSONObject();
                jsonObjectMessage.put("id",message.getId());
                jsonObjectMessage.put("fromUserId", String.valueOf(message.getFromId()));
                jsonObjectMessage.put("toUserId", new String[] {String.valueOf(message.getToId())});
                jsonObjectMessage.put("contentText", String.valueOf(message.getContent()));
                jsonObjectMessage.put("sendTime", message.getSendTime());
                jsonObjectMessage.put("userName",user.getUsername());
                jsonObjectMessage.put("headurl",user.getHeadurl());
                jsonObjectMessage.put("msgType",message.getMsgType());
                System.out.println(jsonObjectMessage);
//                sendMessage(onLineList.get(userId), JSON.toJSONString(ApiReturnUtil.success(jsonObjectMessage)));
                messageService.updateMessage(1, Integer.parseInt(userId));
            }
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        System.out.println("断开连接"+webSocket);
        //在线人数减一
        subOnLineCount();
        //从在线清单中移除当前用户
        onLineList.remove(userId);
    }

    @Override
    public void onMessage(WebSocket webSocket, String jsonMessage) {
        Message messages = new Message();
        if(StringUtils.isNotBlank(jsonMessage)){
            JSONArray list=JSONArray.parseArray(jsonMessage);
            for (int i = 0; i < list.size(); i++) {
                try {
                    //解析发送的报文
                    JSONObject object = list.getJSONObject(i);
                    String toUserId=object.getString("toUserId");
                    String contentText=object.getString("contentText");
                    String fromUserId=object.getString("fromUserId");
                    String msgType = object.getString("msgType");
                    Date day=new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    object.put("fromUserId",this.userId);
                    System.out.println(this.userId);
                    User user = userService.findUserById(Integer.parseInt(fromUserId));
                    object.put("sendTime",df.format(day));
                    object.put("userName",user.getUsername());
                    object.put("headurl",user.getHeadurl());
                    messages.setContent(contentText);
                    messages.setFromId(Integer.parseInt(fromUserId));
                    messages.setToId(Integer.parseInt(toUserId));
                    messages.setMsgType(Integer.valueOf(msgType));
                    //传送给对应用户的websocket
                    if(StringUtils.isNotBlank(toUserId)&&StringUtils.isNotBlank(contentText)){
                        if(onLineList.get(toUserId)!=null && !onLineList.get(toUserId).equals("")){
                            sendMessage(onLineList.get(toUserId),JSON.toJSONString(ApiReturnUtil.success(object)));
                            //此处可以放置相关业务代码，例如存储到数据库
                            messages.setStatus(1);
                            messages.setSendTime(df.format(day));
                            System.out.println(messages);
                            System.out.println(messageService);
                            messageService.addMessage(messages);
                        }else{
                            messages.setStatus(0);
                            messages.setSendTime(df.format(day));
                            System.out.println(messages);
                            System.out.println(messageService);
                            messageService.addMessage(messages);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    public void sendMessage(WebSocket webSocket,String message){
        webSocket.send(message);
    }

    public String getMessage(Message message){
        //使用JSONObject方法构建Json数据
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("fromUserId", String.valueOf(message.getFromId()));
        jsonObjectMessage.put("toUserId", new String[] {String.valueOf(message.getToId())});
        jsonObjectMessage.put("contentText", String.valueOf(message.getContent()));
        jsonObjectMessage.put("type", String.valueOf(message.getMsgType()));
        jsonObjectMessage.put("time", message.getSendTime());
        return jsonObjectMessage.toString();
    }

    public int getOnLineCount(){
        return  this.onLineCount;
    }

    public void addOnLineCount(){
        onLineCount++;
    }

    public void subOnLineCount(){
        onLineCount--;
    }
}
