package com.example.demo.controller;

import com.example.demo.Util.CommunityUtil;
import com.example.demo.Util.ParseExcelUtil;
import com.example.demo.eneity.Course;
import com.example.demo.service.ClassroomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class CourseController {
    @Autowired
    ClassroomService classroomService;
    @PostMapping("/course")
    public String postExcel(MultipartFile file,String term) throws Exception {
        String originalFilename = file.getOriginalFilename();
        // 文件后缀 例如：.png
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // uuid 生成文件名
        String uuid = String.valueOf(UUID.randomUUID());
        // 根路径，在 resources/static/upload
        String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/upload/";
        // 新的文件名，使用uuid生成文件名
        String fileName = uuid + fileSuffix;
        // 创建新的文件
        File fileExist = new File(basePath);
        // 文件夹不存在，则新建
        if (!fileExist.exists()) {
            fileExist.mkdirs();
        }
        // 获取文件对象
        File files = new File(basePath, fileName);
        // 完成文件的上传
        file.transferTo(files);
        List<Course> courses = ParseExcelUtil.parseExcel(files);
        classroomService.sloveExcel(courses,term);
            Map<String,Object> map=new HashMap<>();
            map.put("courseList",courses);
            return CommunityUtil.getJSONString(200,"成功");
    }

}
