package com.example.demo;

import com.example.demo.Util.ParseExcelUtil;
import com.example.demo.eneity.Course;
import com.example.demo.eneity.Goods;
import com.example.demo.eneity.User;
import com.example.demo.service.GoodService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    GoodService goodService;
    @Test
    void contextLoads() {
        System.out.println(goodService.findById(112));
    }
    @Test
    void delete(){
        for (int i = 25; i <32 ; i++) {
            userService.deleteById(i);
        }
    }
    @Test
    void TestExcel(){
//        List<Course> courses = ParseExcelUtil.parseExcel("https://header-1308228782.cos.ap-shanghai.myqcloud.com/2967c0a292dd4b918f1df6031f676f49.xlsx");
//        System.out.println(courses);
    }


    @Test
    public  void initAns(){
       char ans[]=new char[] {'2','1','3'};
        System.out.println(ans);
    }

}
