package com.yiying.vod.controller;

import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.yiying.common.Result;
import com.yiying.vod.service.VodService;
import com.yiying.vod.utils.AliyunVodSDKUtils;
import com.yiying.vod.utils.ConstantPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

@RestController
@RequestMapping("movieVod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("uploadVideoToAliyun")
    public Result uploadVideoToAliyun(MultipartFile file) {
        String vodId = vodService.uploadVideoToAliyun(file);
        return Result.ok().data("videoId", vodId);
    }
    @PostMapping("uploadVideo")
    public Result uploadVideo(MultipartFile file) {
        String newVideopath = vodService.uploadVideo(file);
        return Result.ok().data("newVideopath", newVideopath).data("videoId", RandomUtil.randomString(16));
    }

    @DeleteMapping("{videoId}")
    public Result removeVideo(@PathVariable String videoId) {
        vodService.removeVideo(videoId);
        return Result.ok().message("视频删除成功！");
    }

    @DeleteMapping("deleteVideoBatch")
    public Result deleteVideoBatch(List<String> videoIds) {
        Boolean flag = vodService.deleteVideoBatch(videoIds);
        if (flag) {
            return Result.ok().message("视频删除成功");
        }
        return Result.error();
    }

    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(@PathVariable String videoId) {

        //获取阿里云存储相关常量、
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;

        DefaultAcsClient vodClient = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);

        try {
            GetVideoPlayAuthResponse response = vodClient.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth", playAuth).message("获取凭证成功");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }

        return Result.error();

    }


}

