package com.yiying.movie.entity;

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
@TableName("m_movie_comment")
@ApiModel(value="MMovieComment对象", description="")
public class MMovieComment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberId;

    private String movieId;

    private String content;

    private String nickname;

    private Integer isDelete;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;


}
