package com.yiying.banner.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author qitianfeng
 * @since 2020-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MBanner对象", description="")
public class MBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "图片地址 ")
    private String imageUrl;

    @ApiModelProperty(value = "链接地址 ")
    private String linkUri;

    @ApiModelProperty(value = "标题 ")
    private String title;

    @ApiModelProperty(value = "逻辑删除 1 已经删除 0未删除 ")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间 ")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间 ")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "版本号 ")
    @Version
    private Integer version;


}
