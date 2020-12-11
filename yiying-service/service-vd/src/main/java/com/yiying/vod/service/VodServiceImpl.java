package com.yiying.vod.service;


import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.yiying.config.QiException;
import com.yiying.oss.service.FileService;
import com.yiying.vod.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class VodServiceImpl implements VodService {
    @Reference
    private FileService fileService;
    /**
     * 上传视频文件到阿里云
     *
     * @param file
     * @return
     */
    @Override
    public String uploadVideoToAliyun(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploadVideo = new UploadVideoImpl();
            UploadStreamResponse response = uploadVideo.uploadStream(request);


            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if (StringUtils.isEmpty(videoId)) {
                    throw new QiException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new QiException(20001, "上传失败");
        }
    }

    /**
     * 根据视频id删除视频
     *
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {
        DefaultAcsClient vodClient = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);

        try {
            DeleteVideoResponse response = vodClient.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            e.printStackTrace();
            throw new QiException(20001, "视频删除失败！");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除视频
     *
     * @param videoIds
     * @return
     */
    @Override
    public Boolean deleteVideoBatch(List<String> videoIds) {
        String join = org.apache.commons.lang.StringUtils.join(videoIds.toArray(), ",");

        DefaultAcsClient vodClient = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(join);
        try {
            DeleteVideoResponse response = vodClient.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new QiException(20001, "视频删除失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        if (file.getSize() != 0) {
            //上传的多格式的视频文件-作为临时路径保存，转码以后删除-路径不能写//
            //本地Windows磁盘
            String path = "D:/Projectpicture/websiteimages/temp/";

            //存储在远程服务器
//            String path = "/root/website/temp/";

            File TempFile = new File(path);
            //不存在文件夹，则创建文件
            if (!TempFile.exists()) {
                //创建多个文件夹
                TempFile.mkdirs();
            }

            // 获取上传时候的文件名
            String filename = file.getOriginalFilename();

            // 获取文件后缀名
            String filenameExtension = filename.substring(filename.lastIndexOf(".") + 1);


            //使用hutool工具包生成16个随机字符串做新的文件名，避免中文乱码
            String filename1 = RandomUtil.randomString(16);
            filename = filename1 + "." + filenameExtension;

            //去掉后缀的文件名
            String filename2 = filename.substring(0, filename.lastIndexOf("."));

            //源视频地址+重命名后的视频名+视频后缀
            String remotePath = (path + filename);

            log.info("视频的完整文件名1:" + filename);
            log.info("源视频路径为:" + remotePath);

            //将文件上传到本地磁盘/服务器
            log.info("将文件上传到本地磁盘/服务器");
            InputStream is = null;
            OutputStream os = null;
            try {
                is = file.getInputStream();
                os = new FileOutputStream(new File(path, filename));
                int len = 0;
                byte[] buffer = new byte[2048];

                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭文件
                try {
                    os.close();
                    os.flush();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("文件存储完毕");

            log.info("开始调用转码工具类");
            //调用转码机制flv mp4 f4v m3u8 webm ogg放行直接播放，
            //asx，asf，mpg，wmv，3gp，mov，avi，wmv9，rm，rmvb等进行其他转码为mp4
            if (filenameExtension.equals("avi") || filenameExtension.equals("rm")
                    || filenameExtension.equals("rmvb") || filenameExtension.equals("wmv")
                    || filenameExtension.equals("3gp") || filenameExtension.equals("mov")
                    || filenameExtension.equals("flv") || filenameExtension.equals("ogg")) {
                //调用转码
                run(remotePath);
            }

            //获取转码后的mp4文件名
            String Mp4path = "D:\\Projectpicture\\website\\finshvideo\\";
//            String Mp4path = "/root/websiteimages/finshvideo/";
            filename2 = filename2 + ".mp4";
            String newVideopath = Mp4path + filename2;
            log.info("新视频的url:" + newVideopath);

            //删除临时文件
            File file2 = new File(path);
            if (!file2.exists()) {
                try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (!file2.isDirectory()) {
                try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String[] tempList = file2.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile() || temp.isDirectory()) {
                    temp.delete();        //删除文件夹里面的文件
                }
            }
            log.info("所有的临时视频文件删除成功");
            String s = fileService.uploadToFastDfs(newVideopath);
            return s;
            //将转换完成后的视频路径存储到数据库
            //返回已转码后的视频存放地址
//            return newVideopath;
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static void run(String sourcePath) {
        try {
            // 转码和截图功能开始
            String filePath = sourcePath;                //web传入的源视频

            ConverVideoUtils converVideoUtils = new ConverVideoUtils(filePath);  //传入path
            String targetExtension = ".mp4";                //设置转换的格式
            boolean isDelSourseFile = true;

            //删除源文件
            boolean beginConver = converVideoUtils.beginConver(targetExtension, isDelSourseFile);
            System.out.println(beginConver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
