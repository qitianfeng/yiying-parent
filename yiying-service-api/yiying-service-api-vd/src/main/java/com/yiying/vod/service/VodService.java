package com.yiying.vod.service;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

@Repository
public interface VodService {
    /**
     * 上传视频文件到阿里云
     * @param file
     * @return
     */
    String uploadVideoToAliyun(MultipartFile file);

    /**
     * 根据视频id删除视频
     * @param videoId
     */
    void removeVideo(String videoId);

    Boolean deleteVideoBatch(List<String> videoIds);


    //上传视频
    String uploadVideo(MultipartFile file);


}
