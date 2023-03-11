package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.HostHolder;
import com.example.demo.eneity.*;
import com.example.demo.eneity.vo.MessageVo;
import com.example.demo.eneity.vo.MessageVo2;
import com.example.demo.eneity.vo.MessageVo3;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    GoodService goodService;
    @Autowired
    CommentService commentService;
    @Autowired
    SchoolNewsService schoolNewsService;
    @Autowired
    ProductionService productionService;
    @Autowired
    NoticeService noticeService;



    @GetMapping("/SystemMessage")
    public String SystemMessage(int userId,int msgType){
//        User user = hostHolder.getUser();
        List<Message> messages = messageService.findSystemByToId(userId,msgType);
        if (msgType==4){
            List<Goods> myGood = goodService.findMyGood(userId);
            List<Map<String,Object>>lis1=new ArrayList<>();
            for (Goods index:myGood){
                if (index.getStatus()!=1) continue;
                List<Map<String,Object>> list=new ArrayList<>();
                int count=0;
                for (Message message:messages){
                    Map<String,Object>map=new HashMap<>();
                    MessageVo messageVo = JSONObject.parseObject(message.getContent(), MessageVo.class);
                    if ("purchase".equals(messageVo.getType())&&messageVo.getEntityId()==index.getId()){
                        User user = userService.findUserById(message.getFromId());
                        map.put("userName",user.getUsername());
                        map.put("messageId",message.getId());
                        map.put("userUrl",user.getHeadurl());
                        map.put("userTel",user.getTel());
                        map.put("status",message.getStatus());
                        map.put("purchaseTime",message.getSendTime());
                        map.put("targetId",message.getFromId());
                        if (message.getStatus()==0) count++;
                        list.add(map);
                    }
                }
                if (list==null||list.size()<1) continue;
                Map<String,Object>map1=new HashMap<>();
                map1.put("type","purchase");
                map1.put("goodName",index.getName());
                map1.put("goodId",index.getId());
                map1.put("price",index.getPrice());
                map1.put("goodUrl",index.getPicture());
                map1.put("detail",index.getDetail());
                map1.put("uploadTime",index.getUpload_date());
                map1.put("unReadCount",count);
                map1.put("purchaseDetail",list);
                lis1.add(map1);
            }
            List<Message> list = messageService.findMessageByType(userId, 4);
            List<Map<String,Object>>list1=new ArrayList<>();
            for (Message index:list){
                Map<String,Object>map=new HashMap<>();
                User user = userService.findUserById(index.getFromId());
                MessageVo2 messageVo2 = JSONObject.parseObject(index.getContent(), MessageVo2.class);
                Goods good = goodService.findById(messageVo2.getEntityId());
                if (good==null||user==null||messageVo2.getType().equals("purchase"))continue;
//                System.out.println(messageVo2.getEntityId());
                map.put("sellerId",user.getId());
                map.put("sellerName",user.getUsername());
                map.put("sellerUrl",user.getHeadurl());
                map.put("goodId",good.getId());
                map.put("goodName",good.getName());
                map.put("sendTime",index.getSendTime());
                map.put("goodUrl",good.getPicture());
                if ("successful".equals(messageVo2.getType())){
                    map.put("status",0);
                }else {
                    map.put("status",1);
                }
                list1.add(map);
            }
            Map<String,Object>resultMap=new HashMap<>();
            resultMap.put("result",lis1);
            resultMap.put("result2",list1);
            return CommunityUtil.getJSONString(200,"查询成功",resultMap);
        }
        if (msgType==3){
            List<Map<String,Object>>list=new ArrayList<>();
            for (Message index:messages){
                if (index.getFromId()==userId) continue;
                Map<String,Object>map=new HashMap<>();
//                map.put("index",index);
                MessageVo3 messageVo3 = JSONObject.parseObject(index.getContent(), MessageVo3.class);
//                System.out.println(messageVo3);
                Comment commentById = commentService.findCommentById(messageVo3.getEntityId());
//                System.out.println(commentById);
                Map<String,Object>indexMap=new HashMap<>();
                if (commentById==null) continue;
                //2为表白墙 3为社团 4为食堂
                if (commentById.getCommentType()==2){
                    SchoolNews news = schoolNewsService.findNewsById(commentById.getTargetId());
                    if (news==null) continue;
                    map.put("newsId",news.getId());
                    map.put("createTime",news.getUploadTime());
                    map.put("context",news.getContent());
                    List<String>pictures=new ArrayList<>();
                    pictures.add(news.getPicture1());pictures.add(news.getPicture2());pictures.add(news.getPicture3());
                    pictures.add(news.getPicture4());pictures.add(news.getPicture5());pictures.add(news.getPicture6());
                    pictures.add(news.getPicture7());pictures.add(news.getPicture8());pictures.add(news.getPicture9());
                    map.put("imgList",pictures);
                    indexMap.put("Detail",map);
                    indexMap.put("index",index);
                    indexMap.put("type",2);
//                    map.put("index",index);
//                    list.add(map);
                }else if (commentById.getCommentType()==4){
                    Production productionById = productionService.findProductionById(commentById.getTargetId());
//                    System.out.println(productionById);
                    if (productionById==null) continue;
                    System.out.println(commentById.getTargetId());
                    map.put("proId",commentById.getTargetId());
                    indexMap.put("Detail",map);
                    indexMap.put("index",index);
                    indexMap.put("type",4);

                }else if (commentById.getCommentType()==3){
                    Notice notice = noticeService.findNotcieById(commentById.getTargetId());
//                    System.out.println(notice);
                    if (notice==null) continue;
                    User writer = userService.findUserById(notice.getUserId());
                    List<String>pictures=new ArrayList<>();
                    if (notice.getPicture1()!=null)
                   pictures.add(notice.getPicture1());
                    if (notice.getPicture2()!=null)pictures.add(notice.getPicture2());
                    if (notice.getPicture3()!=null)
                    pictures.add(notice.getPicture3());
                    map.put("groupId",notice.getGroupId());
                   map.put("imgList",pictures);
                   map.put("title",notice.getTitle());
                   map.put("context",notice.getContext());
                   map.put("writerName",writer.getUsername());
                   map.put("writerUrl",writer.getHeadurl());
                   map.put("sendTime",notice.getCreateTime());
                   map.put("noticeId",notice.getId());
                   map.put("commentCount",commentService.selectComment(1,notice.getId(),3).size());
                    indexMap.put("Detail",map);
                    indexMap.put("index",index);
                    indexMap.put("type",3);
                }

                list.add(indexMap);
            }
            Map<String,Object> map=new HashMap<>();
            map.put("SystemMessage",list);
            return CommunityUtil.getJSONString(200,"查询成功",map);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("SystemMessage",messages);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @GetMapping("unReadMessage")
    public String unReadMessage(int userId,int msgType){
//        User user = hostHolder.getUser();
        int count = messageService.countUnreadMessage(userId,msgType);
        HashMap<String ,Object> map=new HashMap<>();
        map.put("count",count);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }

    @PutMapping("/SystemMessage")
    public void PutSystemMessage(int userId,int msgType){
//        User user = hostHolder.getUser();
        messageService.updateMessageStatus(userId,msgType);
    }
    //聊天记录
    @GetMapping(path = "/message")
    public String findMessage(int from_id,int userId,@RequestParam(required = false,defaultValue = "1")int page){
        User user=userService.findUserById(from_id);
        User toUser = userService.findUserById(userId);
        User fromUser = userService.findUserById(from_id);
        List<Message> fromMessages = messageService.findMessage(from_id,toUser.getId());
        List<Message> toMessages = messageService.findMessage(toUser.getId(),from_id);
//        System.out.println(fromMessages);
//        System.out.println(toMessages);
        if((fromMessages==null||fromMessages.size()<1) && (toMessages==null||toMessages.size()<1)){
            Map<String,Object> map=new HashMap<>();
            map.put("username",user.getUsername());
            return (String) CommunityUtil.getJSONString(500,"暂无信息内容",map);
        } else {
            List<Map<String,Object>> list1 = new ArrayList<>();
            List<Map<String,Object>> list2 = new ArrayList<>();
            List<Map<String,Object>> lists = new ArrayList<>();
            for (Message message : fromMessages){
                Map<String,Object> map = new HashMap<>();
                map.put("id",message.getId());
                map.put("contentText",message.getContent());
                map.put("sendTime",message.getSendTime());
                map.put("userName",fromUser.getUsername());
                map.put("headurl",fromUser.getHeadurl());
                map.put("fromUserId",from_id);
                map.put("toUserId",userId);
                map.put("msgType",message.getMsgType());
                list1.add(map);
            }
            for (Message message : toMessages){
                Map<String,Object> map = new HashMap<>();
                map.put("id",message.getId());
                map.put("contentText",message.getContent());
                map.put("sendTime",message.getSendTime());
                map.put("userName",toUser.getUsername());
                map.put("headurl",toUser.getHeadurl());
                map.put("fromUserId",userId);
                map.put("toUserId",from_id);
                map.put("msgType",message.getMsgType());
                list2.add(map);
            }
            lists.addAll(list1);
            lists.addAll(list2);
//        System.out.println(lists);
            Collections.sort(lists, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if (o1.get("sendTime").toString().compareTo(o2.get("sendTime").toString())<0)
                        return 1;
                    else if (o1.get("sendTime").toString().compareTo(o2.get("sendTime").toString())>0) return -1;
                    return 0;
                }
            });
//        System.out.println(lists);
            int total;
            if (lists.size()%12==0)
                total=lists.size()/12;
            else total=lists.size()/12+1;
//            if (page>total)return CommunityUtil.getJSONString(500,"非法访问");
            List<Map<String,Object>> alllist = new ArrayList<>();
            List<Map<String,Object>> formlist = new ArrayList<>();
            List<Map<String,Object>> tolist = new ArrayList<>();
            if (page==total){
                alllist=lists.subList((page-1)*12,lists.size());
            }else{
                alllist=lists.subList((page-1)*12,page*12);
                //分组
            }
            System.out.println(alllist);
            for(int i = 0;i < alllist.size();i++) {
                Map<String, Object> map = alllist.get(i);
                if(map.get("fromUserId").equals(from_id)){
                    formlist.add(map);
                    System.out.println(formlist);
                }else
                    tolist.add(map);
                System.out.println(tolist);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("total",total);//总页数
            map.put("Current",page);
            map.put("fromMessage",formlist);
            map.put("toMessage",tolist);
            map.put("username",user.getUsername());

            return (String) CommunityUtil.getJSONString(200, "信息", map);
        }
    }
    @PostMapping(path = "/message")
    public String addMessage(int to_id,String content,int userId){
//        User user = hostHolder.getUser();
        MsgStatus msgStatus = messageService.findMsgStatus(userId, to_id);
        if (msgStatus!=null){
            messageService.updateMsg(msgStatus.getId(),0);
        }
        Message message = new Message();
        message.setContent(content);
        message.setFromId(userId);
//        message.setFrom_id(5);
        message.setToId(to_id);
        message.setMsgType(0);
        message.setStatus(0);
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.setSendTime(df.format(day));
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(200,"发送成功");
    }
    @GetMapping(path = "/unReadmessage")
    public String unReadmessage(int from_id,int msg_type,int userId){
//        User user = hostHolder.getUser();
        if (msg_type==0){
            Map<String,Object> map = new HashMap<>();
            List list = messageService.unReadmessage(from_id,userId,msg_type);
            map.put("未读消息",list);
            return CommunityUtil.getJSONString(200,"未读",map);
        }else
            return CommunityUtil.getJSONString(500,"没有未读消息");
    }
    @GetMapping(path = "/getMessage")
    public String getMessage(int userId){
        User user = userService.findUserById(userId);
        List<Integer> strList = new ArrayList<Integer>();
        List<Integer> strList1 = new ArrayList<Integer>();
        strList = messageService.getMessage(userId);
        strList1 = messageService.getFromMessage(userId);
//        System.out.println(strList);
//        System.out.println(strList1);
        Set<Integer>s1=new HashSet<>();
        s1.addAll(strList);
        s1.addAll(strList1);
        List<Integer>lists=new ArrayList<>(s1);
        System.out.println(lists);
        System.out.println(lists.size());
        List<Map<String,Object>>list=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            System.out.println(lists.get(i));
            user = userService.findUserById(lists.get(i));
            if (user==null) continue;
            int count = messageService.unmessage(user.getId(),userId);
            List<Message> fromMessages = messageService.findMessage(user.getId(),userId);
            List<Message> toMessages = messageService.findMessage(userId,user.getId());
            fromMessages.addAll(toMessages);
            List<Map<String,Object>> newlist = new ArrayList<>();
            for (Message message : fromMessages){
                Map<String,Object> map = new HashMap<>();
                map.put("contentText",message.getContent());
                map.put("sendTime",message.getSendTime());
                newlist.add(map);
            }
            Collections.sort(newlist, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if (o1.get("sendTime").toString().compareTo(o2.get("sendTime").toString())>0)
                        return -1;
                    else if (o1.get("sendTime").toString().compareTo(o2.get("sendTime").toString())<0) return 1;
                    return 0;
                }
            });
            MsgStatus msgStatus = messageService.findMsgStatus(userId, lists.get(i));
            if (msgStatus==null){
                MsgStatus msg = new MsgStatus();
                msg.setFromId(userId);msg.setToId( lists.get(i));msg.setStatus(0);
                messageService.addMsg(msg);
            }else {
                if (msgStatus.getStatus()==1)
                    continue;
            }
            Map<String,Object> map = new HashMap<>();
            Map<String,Object> map1 = new HashMap<>();
            if(newlist.size()>0)
            map1=newlist.get(0);
            System.out.println(map1);
            if (map1.isEmpty()) {
                map1.put("contentText","");
            }
            else if (map1.get("contentText").toString().contains("<div")&&map1.get("contentText").toString().contains("</div>")) map1.put("contentText","【动画表情】");
            else if (map1.get("contentText").toString().contains("https://header")&&(map1.get("contentText").toString().contains(".jpg")||map1.get("contentText").toString().contains(".png")||map1.get("contentText").toString().contains(".jpeg")))
                map1.put("contentText","【图片】");
            map.put("fromUserId",user.getId());
            map.put("fromUserName",user.getUsername());
            map.put("fromHeadurl",user.getHeadurl());
            map.put("unReadCount",count);
            map.put("contentText",map1.get("contentText"));
            map.put("sendTime",map1.get("sendTime"));
            list.add(map);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("talkList",list);
//        System.out.println(map)
        return CommunityUtil.getJSONString(200,"请求成功",map);
    }
        @GetMapping("/countUnreadMessage")
    public String countUnreadMessage(int userId){
        int count = messageService.countUnreadMessage(userId);
//            List<Message> unreadMessage = messageService.findUnreadMessage(userId);
            HashMap<String ,Object> map=new HashMap<>();
        map.put("count",count);
//        map.put("unreadMessage",unreadMessage);
        return CommunityUtil.getJSONString(200,"查询成功",map);
    }
    @PutMapping("talkRecord")
    public String deleteTalk(int userId,int toId){
        User userById = userService.findUserById(userId);
        User userById1 = userService.findUserById(toId);
        if (userById!=null&&userById1!=null){
            MsgStatus msgStatus = messageService.findMsgStatus(userId, toId);
            messageService.updateMsg(msgStatus.getId(),1);
        }else {
            return CommunityUtil.getJSONString(500,"删除失败");
        }
        return CommunityUtil.getJSONString(200,"删除成功");
    }
    @GetMapping(path = "/getChat")
    public String getChat (String toId){
        User toUesr = userService.findByStunum(toId);
        if(toUesr==null){
            return CommunityUtil.getJSONString(404,"找不到该用户");
        }
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>>list=new ArrayList<>();
        map.put("userId",toUesr.getId());
        map.put("userName",toUesr.getUsername());
        map.put("headurl",toUesr.getHeadurl());
        list.add(map);
        Map<String,Object> toUserMap = new HashMap<>();
        toUserMap.put("userMessage",list);
        return CommunityUtil.getJSONString(200,"请求成功",toUserMap);
    }
    @PutMapping("/purchaseMessage")
        public String updateMessageStatus(int userId,int goodId,int msgType){
        if (msgType==4){
            List<Message> messages = messageService.findSystemByToId(userId, msgType);
            for (Message index: messages){
                MessageVo messageVo = JSONObject.parseObject(index.getContent(), MessageVo.class);
                if (index.getStatus()==0&&messageVo.getEntityId()==goodId){
                    messageService.updateMsg(index.getId(),1);
                }
            }
        }
        return CommunityUtil.getJSONString(200,"修改成功");
    }
}
