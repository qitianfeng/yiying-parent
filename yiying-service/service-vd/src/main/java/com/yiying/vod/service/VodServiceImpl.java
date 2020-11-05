package com.yiying.vod.service;


import com.aliyun.oss.ClientException;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.yiying.VodService;
import com.yiying.config.QiException;
import com.yiying.vod.utils.AliyunVodSDKUtils;
import com.yiying.vod.utils.ConstantPropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
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
            if(!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(StringUtils.isEmpty(videoId)){
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
            throw new QiException(20001,"视频删除失败！");
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
            throw new QiException(20001,"视频删除失败");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
