package com.yiying.movie.entity;

import java.math.BigDecimal;

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
 * @author testjava
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MMovie对象", description="")
public class MMovie implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "影片唯一标识id")
    @TableId(value = "movie_id", type = IdType.ID_WORKER_STR)
    private String movieId;

    @ApiModelProperty(value = "影片的演员列表")
    private String actors;

    @ApiModelProperty(value = "影片的拍摄国家")
    private String country;

    @ApiModelProperty(value = "影片的导演列表")
    private String directors;


    @ApiModelProperty(value = "影片的剧情概要")
    private String plotSimple;

    @ApiModelProperty(value = "影片的对白使用的语言")
    private String language;

    @ApiModelProperty(value = "影片的海报")
    private String poster;


    @ApiModelProperty(value = "影片的得分")
    private String rating;

    @ApiModelProperty(value = "影片的评分人数")
    private Integer ratingCount;

    @ApiModelProperty(value = "影片的上映时间")
    private String releaseDate;

    @ApiModelProperty(value = "影片的持续时间")
    private String runtime;

    @ApiModelProperty(value = "影片的名称")
    private String title;

    @ApiModelProperty(value = "影片的编剧列表")
    private String wriyer;

    @ApiModelProperty(value = "影片的拍摄年代")
    private String year;

    @ApiModelProperty(value = "云端视频资源")
    private String viedoSourceId;

    @ApiModelProperty(value = "是否可以试听：1收费 0免费")
    private Integer isFree;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "购票价格")
    private BigDecimal price;

    @ApiModelProperty(value = "在线播放的购买价格")
    private BigDecimal onlinePrice;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 1 已经删除 0未删除")
    private Integer isDeleted;

    @Version
    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "电影分类id")
    private String subjectId;

    @ApiModelProperty(value = "电影分类父id")
    private String subjectParentId;
    @ApiModelProperty(value = "电影分类id")
    private String subjectYearId;

    @ApiModelProperty(value = "电影分类父id")
    private String subjectYearParentId;
    @ApiModelProperty(value = "电影分类id")
    private String subjectGeneresId;

    @ApiModelProperty(value = "电影分类父id")
    private String subjectGeneresParentId;

    private String status;


}
