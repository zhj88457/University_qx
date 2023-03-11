package com.example.demo.Util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class  PictureLoadUtil {

    @Value("${cos.accessKey}")
    private String accessKey;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.bucket}")
    private String bucket;
    @Value("${cos.bucketName}")
    private String bucketName;
    @Value("${cos.path}")
    private String path;

    public List<String> loadPictureList(List<MultipartFile> files){
        List<String>list=new ArrayList<>();
        for (MultipartFile file:files){
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = CommunityUtil.generateUUID()+eName;
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String bucketName = this.bucketName;

            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/"+newFileName;
                list.add("https://header-1308228782.cos.ap-shanghai.myqcloud.com"+key);
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }
        return list;
    }

    public String loadPicture(MultipartFile file){
        String url=null;
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = CommunityUtil.generateUUID()+eName;
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp",null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/"+newFileName;
             url="https://header-1308228782.cos.ap-shanghai.myqcloud.com"+key;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
        return url;
    }

}
