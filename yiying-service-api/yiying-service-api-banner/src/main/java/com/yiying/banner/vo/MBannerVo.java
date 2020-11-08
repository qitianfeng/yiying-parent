package com.yiying.banner.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

public class MBannerVo {

    @ApiModelProperty(value = "图片地址 ")
    private String imageUrl;

    @ApiModelProperty(value = "链接地址 ")
    private String linkUri;

    @ApiModelProperty(value = "标题 ")
    private String title;
}
