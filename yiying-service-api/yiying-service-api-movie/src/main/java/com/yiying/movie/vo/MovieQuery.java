package com.yiying.movie.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MovieQuery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电影名称")
    private String title;


    @ApiModelProperty(value = "一级类别id")
    private String subjectParentId;

    @ApiModelProperty(value = "二级类别id")
    private String subjectId;
}
