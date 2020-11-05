package com.yiying.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("m_movie_play_hall")
@ApiModel(value="MMoviePlayHall对象", description="")
public class MMoviePlayHall implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "播放电影id")
    private String movieId;

    @ApiModelProperty(value = "观看厅的id ")
    private String watchHallId;

    @ApiModelProperty(value = "场次 ")
    private String changci;

    @ApiModelProperty(value = "开始使用时间 ")
    private Date startUseTime;

    @ApiModelProperty(value = "结束使用时间 ")
    private Date endUseTime;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "座位信息")
    private String seats;


}
