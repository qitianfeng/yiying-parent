package com.yiying.order.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@Data
@ApiModel(value="MOrder对象", description="")
public class OrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "会员ID")
    private String memberId;
    @ApiModelProperty(value = "电影ID")
    private String movieId;

    @ApiModelProperty(value = "电影名字")
    private String movieTitle;

    @ApiModelProperty(value = "电影海报")
    private String moviePoster;

    @ApiModelProperty(value = "支付类型（1：支付宝，2：其他）")
    private Integer payType;

    @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @ApiModelProperty(value = "会员手机")
    private String mobile;

    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "订单状态（0：未支付，1：已支付）")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
