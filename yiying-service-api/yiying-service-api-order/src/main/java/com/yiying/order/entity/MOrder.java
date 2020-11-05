package com.yiying.order.entity;

import java.math.BigDecimal;
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
 * @author qitianfeng
 * @since 2020-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MOrder对象", description="")
public class MOrder implements Serializable {

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
    private Boolean isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;


}
