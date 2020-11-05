package com.yiying.order.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value="OrderTicketVo对象", description="")
public class OrderTicketVo {
    //电影的类型
    private String genres;
    //电影的播放时长
    private String runtime;
    //电影的场次 ==============> 播放的日期 + 播放的展厅
    private String changci;
    //电影的票价
    private BigDecimal price;

    //电影播放的影厅
    private String playHall;

    //电影海报
    private String poster;

    //电影名称
    private String title;
}
