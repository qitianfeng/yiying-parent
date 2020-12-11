package com.yiying.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 输出文件到oss
     * @param file
     * @return
     */
    String upload(MultipartFile file);
    /**
     * 输出文件到fastdfs
     * @param fileStr
     * @return
     */
    String uploadToFastDfs(String fileStr);
}
