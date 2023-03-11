package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.eneity.*;
import com.example.demo.eneity.vo.ContributionVo;
import com.example.demo.eneity.vo.GoodsVo2;
import com.example.demo.eneity.vo.MemberVo;
import com.example.demo.service.*;
import com.sun.activation.registries.MailcapFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    GoodService goodService;
    @Autowired
    ProductionService productionService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    MemberService memberService;
    @Autowired
    ContributionService contributionService;
    @Autowired
    SchoolNewsService schoolNewsService;
    @Autowired
    BuildingService buildingService;
    @Autowired
    ClassroomService classroomService;
    @GetMapping(value = {"/","/login"})
    public String login(){
        return "login";
    }
    @PostMapping("login")
    public String ToIndex(String username, String password, Model model){
        User user = userService.findByStunum(username);
        System.out.println(user);
        if (user==null||user.getStatus()!=2){
            model.addAttribute("msg","用户名不存在或权限不够");
            return "login";
        }
        String userPassword= CommunityUtil.md5(password+ user.getSalt());
        if (!user.getPassword().equals(userPassword)){
            model.addAttribute("msg","用户名或者密码错误");
        }
        return "main";
    }
    @GetMapping("/Auser")
    public String ToStudent(Model model,@RequestParam(defaultValue = "1") int page){
        List<User> users = userService.findUsers();
        int total=users.size()/10;
        if (total%10!=0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("users",users.subList((page-1)*10,users.size()));
        model.addAttribute("users",users.subList((page-1)*10,page*10>users.size()?users.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",users.size());
        return "user-list";
    }
    @GetMapping("/deleteuser")
    public void deleUser(int uid, HttpServletResponse response) throws IOException {
        userService.deleteById(uid);
        response.sendRedirect("/university/Auser");
    }
    @GetMapping("/resetUserPwd")
    public void resetPwd(int uid,HttpServletResponse response) throws IOException {
        User userById = userService.findUserById(uid);
        String password=CommunityUtil.md5("123456"+userById.getSalt());
        userService.resetPwd(uid, password);
        response.sendRedirect("/university/Auser");
    }
    @PostMapping("/deleSome")
    public void deleSome(String uids, HttpServletResponse response) throws IOException {
        String[] split = uids.split(",");
        for (String index: split){
            User user = userService.findUserById(Integer.valueOf(index));
            if (user!=null)
            userService.deleteById(Integer.valueOf(index));
        }
        response.sendRedirect("/university/Auser");
    }
    @GetMapping("/Agroups")
    public String AGroups(Model model,@RequestParam(defaultValue = "1") int page){
        List<Groups> groups = groupService.findAll();
        int total=groups.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("groups",groups.subList((page-1)*10,groups.size()));
        model.addAttribute("groups",groups.subList((page-1)*10,page*10>groups.size()?groups.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",groups.size());
        return "group-list";
    }
    @GetMapping("/AgroupStatus")
    public void groupStatus(int gid,int status,HttpServletResponse response) throws IOException {
        groupService.updateStatus(status,gid);
        Groups group = groupService.findGroupById(gid);
        if(status==1){
            Member member = memberService.findMember(group.getUserId(), gid);
            if (member==null){
                Member member1 = new Member();
                member1.setUserId(group.getUserId());
                member1.setStatus(2);
                member1.setGroupId(gid);
                member1.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                memberService.insertMember(member1);
            }
        }
        response.sendRedirect("/university/Agroups");
    }
    @PostMapping("/deleSomegroup")
    public void deleSomegroup(String gids,HttpServletResponse response) throws IOException {
        String[] split = gids.split(",");
        for (String index:split){
            Groups group = groupService.findGroupById(Integer.valueOf(index));
            if (group!=null)
                groupService.deleteGroup(Integer.valueOf(index));
        }
        response.sendRedirect("/university/Agroups");
    }
    @GetMapping("/AGoods")
    public String AGoods(Model model,@RequestParam(defaultValue = "1") int page){
        List<Goods> all = goodService.findallGoods();
//        System.out.println(all);
        List<GoodsVo2>list=new ArrayList<>();
        for (Goods index:all){
            GoodsVo2 goodsVo2 = new GoodsVo2(index);
            goodsVo2.setSellerName(userService.findUserById(index.getSellerId()).getUsername());
            goodsVo2.setUpload_date(goodService.findDate(index.getId()));
            list.add(goodsVo2);
        }
        int total=list.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        model.addAttribute("goods",list.subList((page-1)*10,page*10>list.size()?list.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",list.size());
        return "goods-list";
    }
    @GetMapping("AUpdateGoods")
    public void AUpdateGoods(int status,HttpServletResponse response,int gid) throws IOException {
        if (status==3)
            goodService.updateStatus(gid);
        response.sendRedirect("/university/AGoods");
    }
    @PostMapping("/deleSomegoods")
    public void deleSomegoods(String gids,HttpServletResponse response) throws IOException {
        String[] split = gids.split(",");
        for (String index:split){
            Goods group = goodService.findById(Integer.valueOf(index));
            if (group!=null)
                goodService.deleteGoods(Integer.valueOf(index));
        }
        response.sendRedirect("/university/AGoods");
    }
    @GetMapping("AProduction")
    public String AProduction(Model model,@RequestParam(defaultValue = "1") int page){
        List<Production> productions = productionService.findAll();
        int total=productions.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("productions",productions.subList((page-1)*10,productions.size()));
        model.addAttribute("productions",productions.subList((page-1)*10,page*10>productions.size()?productions.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",productions.size());
        return "production-list";
    }
    @GetMapping("/deleteAPro")
    public void deleteAPro(int pid,HttpServletResponse response) throws IOException {
        productionService.deletePro(pid);
        response.sendRedirect("/university/AProduction");
    }
    @PostMapping("/deleSomepro")
    public void deleSomepro(String pids,HttpServletResponse response) throws IOException {
        String[] split = pids.split(",");
        for (String index:split){
            Production group = productionService.findProductionById(Integer.valueOf(index));
            if (group!=null)
                productionService.deletePro(Integer.valueOf(index));
        }
        response.sendRedirect("/university/AProduction");
    }
    @GetMapping("/ANotice")
    public String ANotice(int gid,Model model,@RequestParam(defaultValue = "1") int page){
        List<Notice> notices = noticeService.findNotices(gid);
        int total=notices.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("notices",notices.subList((page-1)*10,notices.size()));
        model.addAttribute("notices",notices.subList((page-1)*10,page*10>notices.size()?notices.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",notices.size());
        return "notcie-list";
    }
    @GetMapping("/deleNotice")
    public void deleNotice(int nid,HttpServletResponse response) throws IOException {
        Notice notcie = noticeService.findNotcieById(nid);
        noticeService.deleteNotice(nid);
        response.sendRedirect("/university/ANotice?gid="+notcie.getGroupId());
    }
    @PostMapping("/deleSomenotice")
    public void deleSomenotice(String nids,HttpServletResponse response) throws IOException {
        Notice notcie = new Notice();
        String[] split = nids.split(",");
        for (String index:split){
            notcie=noticeService.findNotcieById(Integer.valueOf(index));
            Notice group = noticeService.findNotcieById(Integer.valueOf(index));
            if (group!=null)
                noticeService.deleteNotice(Integer.valueOf(index));
        }
        response.sendRedirect("/university/ANotice?gid="+notcie.getGroupId());
    }
    @GetMapping("/AMember")
    public String Amember(int gid,Model model,HttpServletResponse response,@RequestParam(defaultValue = "1") int page){
        List<Member> members = memberService.findMemberByGoup(gid);
        List<MemberVo>memberVos=new ArrayList<>();
        for (Member index:members){
            MemberVo memberVo = new MemberVo();
            memberVo.setId(index.getId());
            memberVo.setCreateTime(index.getCreateTime());
            memberVo.setHeadUrl(userService.findUserById(index.getUserId()).getHeadurl());
            memberVo.setStatus(index.getStatus());
            memberVo.setUsername(userService.findUserById(index.getUserId()).getUsername());
            memberVos.add(memberVo);
        }
        int total=memberVos.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("members",memberVos.subList((page-1)*10,memberVos.size()));
        model.addAttribute("members",memberVos.subList((page-1)*10,page*10>memberVos.size()?memberVos.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",memberVos.size());
        return "member-list";
    }
    @GetMapping("/author")
    public void author(int mid,int status,HttpServletResponse response) throws IOException {
        memberService.updateById(mid, status);
        response.sendRedirect("/university/AMember?gid="+memberService.findMemberById(mid).getGroupId());
    }
    @GetMapping("/Acontribution")
    public String Acontribution(Model model,@RequestParam(defaultValue = "1") int page){
        List<Contribution> all = contributionService.findAll();
        List<ContributionVo>list=new ArrayList<>();
        for (Contribution index:all){
            ContributionVo contributionVo = new ContributionVo(index);
            if (userService.findUserById(index.getUserId())==null) continue;
            contributionVo.setUsername(userService.findUserById(index.getUserId()).getUsername());
            list.add(contributionVo);
        }
        int total=list.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("contribution",list.subList((page-1)*10,list.size()));
        model.addAttribute("contribution",list.subList((page-1)*10,page*10>list.size()?list.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",list.size());
        return "contribution-list";
    }
    @GetMapping("/deleteContribu")
    public void deleteContribu(int cid,HttpServletResponse response) throws IOException {
        contributionService.deleteContribution(cid);
        response.sendRedirect("/university/Acontribution");
    }
    @PostMapping("/deleSomecon")
    public void deleSomecon(String cids,HttpServletResponse response) throws IOException {
        String[] split = cids.split(",");
        for (String index:split){
            contributionService.deleteContribution(Integer.valueOf(index));
        }
        response.sendRedirect("/university/Acontribution");
    }
    @GetMapping("/ANews")
    public String ANews(Model model,@RequestParam(defaultValue = "1") int page){
        List<SchoolNews> all =schoolNewsService .findAll();
        int total=all.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("News",all.subList((page-1)*10,all.size()));
        model.addAttribute("News",all.subList((page-1)*10,page*10>all.size()?all.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",all.size());
        return "news-list";
    }
    @GetMapping("/deleteNews")
    public void deleteNews(int nid,HttpServletResponse response) throws IOException {
        schoolNewsService.deleteNews(nid);
        response.sendRedirect("/university/ANews");
    }
    @GetMapping("/buildNews")
    public String buildNews(Model model,@RequestParam(defaultValue = "1") int page){
        List<Contribution> all =contributionService .findAll();
        int total=all.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("Contribution",all.subList((page-1)*10,all.size()));
        model.addAttribute("Contribution",all.subList((page-1)*10,page*10>all.size()?all.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",all.size());
        return "build-news";
    }
    @PostMapping("/build")
    public void build(String pictures,HttpServletResponse response,String context) throws IOException {
        SchoolNews schoolNews = new SchoolNews();
        schoolNews.setContent(context);
        schoolNews.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String[] split = pictures.split(",");
        schoolNews.setPicture1(split[0]);schoolNews.setPicture2(split[1]);schoolNews.setPicture3(split[2]);schoolNews.setPicture4(split[3]);schoolNews.setPicture5(split[4]);schoolNews.setPicture6(split[5]);schoolNews.setPicture7(split[6]);schoolNews.setPicture8(split[7]);schoolNews.setPicture9(split[8]);
        schoolNewsService.addSchoolnews(schoolNews);
        response.sendRedirect("/university/buildNews");
    }
    @GetMapping("/building")
    public String building(Model model,@RequestParam(defaultValue = "1") int page){
        List<Building> all = buildingService.findAll();
        int total=all.size()/10;
        if (total%10!=0||total==0) total++;
        int pre=0;
        int next=0;
        if (page>1) pre=1;
        if (page<total) next=1;
        model.addAttribute("pre",pre);
        model.addAttribute("next",next);
        model.addAttribute("total",total);
        if (page==total)
            model.addAttribute("building",all.subList((page-1)*10,all.size()));
        model.addAttribute("building",all.subList((page-1)*10,page*10>all.size()?all.size():page*10));
        model.addAttribute("page",page);
        model.addAttribute("size",all.size());
        return "building-list";
    }
    @GetMapping("/deletebuild")
    public void deletebuild(int bid,HttpServletResponse response) throws IOException {
        buildingService.deleteById(bid);
        response.sendRedirect("/university/building");
    }
    @GetMapping("/ToAddBuild")
    public String ToAddBuild(){
        return "addBuild";
    }
    @PostMapping("/addBuild")
    public void addBuild(Building building,HttpServletResponse response) throws IOException {
        buildingService.addBuilding(building);
        response.sendRedirect("/university/building");
    }
    @GetMapping("/AClass")
    public String upload(){
        return "class-add";
    }
    @GetMapping("/main")
    public String mains(){
        return "main";
    }
}
