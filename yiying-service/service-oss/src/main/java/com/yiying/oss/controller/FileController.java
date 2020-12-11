package com.yiying.oss.controller;


import com.yiying.common.Result;
import com.yiying.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api("阿里云oss上传文件管理")
@RestController
//@CrossOrigin
@RequestMapping("/movieoss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public Result upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        //返回r对象
        return Result.ok().message("文件上传成功").data("url", uploadUrl);
    }
    @ApiOperation(value = "文件上传")
    @PostMapping("upload1")
    public Result upload1(
            String str) {
        String uploadUrl = fileService.uploadToFastDfs(str);
        //返回r对象
        return Result.ok().message("文件上传成功").data("url", uploadUrl);
    }
}
