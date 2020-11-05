package com.yiying.movie.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovieVo {

    private String title;
    private String poster;
    private String plotSimple;
    private String subjectParentId;
    private String subjectId;
    private String subjectYearParentId;
    private String subjectYearId;
    private String subjectGeneresId;

    private String subjectGeneresParentId;
    private String language;

    private String directors;
    private String actors;
    private String country;
    private String wriyer;
    private String year;
    private String[] date;
    private BigDecimal price;
    private String subjectHallId;

}
