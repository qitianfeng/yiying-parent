package com.yiying.movie.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-01
 */
@Data
public class MovieVideoVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "电影Id")
    private String movieId;

    @ApiModelProperty(value = "电影名称")
    private String title;

    @ApiModelProperty(value = "上传的远程Id")
    private String videoSourceId;

    @ApiModelProperty(value = "在远程的名称")
    private String videoOriginalName;


}
