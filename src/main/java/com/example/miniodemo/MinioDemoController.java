package com.example.miniodemo;


import com.example.miniodemo.config.MinioConfig;
import com.example.miniodemo.util.MinioUtil;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class MinioDemoController {

    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;

    //查看存储bucket是否存在
    @GetMapping("/bucketExists")
    public Boolean bucketExists(@RequestParam("bucketName") String bucketName) {
        return minioUtil.bucketExists(bucketName);
    }

    //创建存储bucket
    @GetMapping("/makeBucket")
    public Boolean makeBucket(String bucketName) {
        return minioUtil.makeBucket(bucketName);
    }

    //删除存储bucket
    @GetMapping("/removeBucket")
    public Boolean removeBucket(String bucketName) {
        return minioUtil.removeBucket(bucketName);
    }

    //获取全部bucket
    @GetMapping("/getAllBuckets")
    public List<Bucket> getAllBuckets() {
        List<Bucket> allBuckets = minioUtil.getAllBuckets();
        return allBuckets;
    }

    //文件上传返回url
    @PostMapping("/upload")
    public String upload() {
//        String objectName = minioUtil.upload(file);
//        if (null != objectName) {
//            return prop.getMinioEndpoint() + "/" + prop.getBucketName() + "/" + objectName;
//        }
//        return null;
        File file = new File("C:\\Users\\cc\\Desktop\\cc\\**.png");
        try (FileInputStream fis = new FileInputStream(file)) {
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "image/jpeg", fis);
            return minioUtil.upload(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //图片/视频预览
    @GetMapping("/preview")
    public String preview(@RequestParam("fileName") String fileName) {
        return minioUtil.preview(fileName);
    }

    //文件下载
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse res) {
        minioUtil.download(fileName, res);
    }

    //根据url地址删除文件
    @PostMapping("/delete")
    public String remove(String url) {
        String objName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        minioUtil.remove(objName);
        return objName;
    }

}
