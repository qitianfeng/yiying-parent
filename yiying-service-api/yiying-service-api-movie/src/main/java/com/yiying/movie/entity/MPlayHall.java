package com.yiying.movie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.generator.config.po.TableFill;
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
@ApiModel(value="MPlayHall对象", description="")
public class MPlayHall implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "观看厅的id")
    @TableId(value = "watch_hall_id", type = IdType.ID_WORKER_STR)
    private String watchHallId;

    @ApiModelProperty(value = "观看厅的名字")
    private String title;

    @ApiModelProperty(value = "父Id")
    private String parentId;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
