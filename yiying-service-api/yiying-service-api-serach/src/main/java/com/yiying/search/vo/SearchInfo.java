package com.yiying.search.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Document(indexName = "moviesearchinfo",type = "movie")
@Data
public class SearchInfo {
    @Id
    private String movieId;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    @ApiModelProperty(value = "电影标题")
    private String title;

    @ApiModelProperty(value = "电影价格")
    @Field(type = FieldType.Double)
    private BigDecimal price;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @Field(type = FieldType.Date)
    private Date gmtModified;

    //动态的域的添加和变化 json数据
    @Field(type = FieldType.Text)
    private String subjectSpec;

    @ApiModelProperty(value = "电影封面图片路径")
    @Field(type = FieldType.Text)
    private String poster;

    //默认排序
    @ApiModelProperty(value = "评分")
    private Long rating;

    //默认排序
    @ApiModelProperty(value = "评分人数")
    private Integer ratingCount;

    //默认排序
    @ApiModelProperty(value = "语言")
    private String language;

    //全部规格参数
    @Field(type = FieldType.Object)
    private Map<String, Object> specMap;


}
