package com.yiying.oss.service;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.yiying.oss.util.FastDFSClient;
import com.yiying.oss.util.FastDFSFile;
import com.yiying.oss.utils.ConstantPropertiesUtil;
import com.yiying.oss.utils.MappedBiggerFileReader;
import org.apache.dubbo.config.annotation.Service;
import org.apache.http.entity.ContentType;
import org.joda.time.DateTime;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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
            ossClient.putObject(bucketName, fileUrl, inputStream);


            // 关闭OSSClient。
            ossClient.shutdown();

            //获取url地址
            urlUpload = "http://" + bucketName + "." + endPoint + "/" + fileUrl;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return urlUpload;
    }


    /**
     * 输出文件到fastdfs
     *
     * @param fileStr
     * @return
     */
    @Override
    public String uploadToFastDfs(String fileStr) {

//        MappedBiggerFileReader reader=null;
        FastDFSFile fastdfsfile = null;
        File file1 = null;
        try {
//            reader = new MappedBiggerFileReader(fileStr, 65536);
//            while (reader.read() != -1) ;

            file1 = new File(fileStr);
            MultipartFile file = getMultipartFile(file1);
            fastdfsfile = new FastDFSFile(
                    file.getOriginalFilename(),//原来的文件名  1234.jpg
                    file.getBytes(),//文件本身的字节数组
                    StringUtils.getFilenameExtension(file.getOriginalFilename())
            );


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//                reader.close();
//            file1.delete();
        }


        String[] upload = FastDFSClient.upload(fastdfsfile);


        return FastDFSClient.getTrackerUrl() + "/" + upload[0] + "/" + upload[1];
    }

    private static MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
}
