package com.yiying.oss.service;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.yiying.oss.utils.ConstantPropertiesUtil;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {


    /**
     * 输出文件到oss
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String fileHost = ConstantPropertiesUtil.FILE_HOST;

        String urlUpload = null;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限，
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }

            //获取上传文件流
            InputStream inputStream = file.getInputStream();
            //构建文件日期
            String filePath = new DateTime().toString("yyyy/MM/dd");

            //文件扩展名
            String originalFilename = file.getOriginalFilename();

            String fileName = RandomUtil.randomString(10);

            //获取文件类型
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFileName = fileName + fileType;

            String fileUrl = filePath + "/" + newFileName;

            //上传到阿里云oss中
            ossClient.putObject(bucketName,fileUrl,inputStream);


            // 关闭OSSClient。
            ossClient.shutdown();

            //获取url地址
            urlUpload = "http://" + bucketName + "." + endPoint + "/" + fileUrl;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return urlUpload;
    }
}
