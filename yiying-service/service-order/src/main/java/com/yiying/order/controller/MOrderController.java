package com.yiying.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.javafx.collections.MappingChange;
import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import com.yiying.order.vo.OrderTicketVo;
import com.yiying.order.vo.Params;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@RestController
@RequestMapping("/order")
public class MOrderController {

    @Autowired
    private MOrderService orderService;


    @PostMapping("createOrder/{movieId}")
    public Result createOrder(@PathVariable String movieId, HttpServletRequest request) {

        String jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(jwtToken)) {
            return Result.error().message("请登录后再购买");
        }
        //根据课程id和用户id创建订单
        String orderId = orderService.createOrder(movieId, jwtToken);
        return Result.ok().data("orderId", orderId);
    }


    @PostMapping("createOrdersTicket/{movieId}")
    public Result createOrdersTicket(@PathVariable String movieId, HttpServletRequest request) {

        String jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(jwtToken)) {
            return Result.error().message("请登录后再购买");
        }
        //根据课程id和用户id创建订单
        String orderId = orderService.createOrdersTicket(movieId, jwtToken);
        return Result.ok().data("orderId", orderId);
    }



    @GetMapping("getOrderInfo/{orderId}")
    public Result getMOrderInfo(@PathVariable String orderId) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, orderId);
        MOrder one = orderService.getOne(wrapper);
        return Result.ok().data("order", one);
    }


    /**
     * 通过电影票购买的，页面需要展示相应电影的播放展示厅，电影的基本信息
     * @param orderId
     * @return
     */
    @GetMapping("getOrderInfoByTicket/{orderId}")
    public Result getOrderInfoByTicket(@PathVariable String orderId) {

        Map<String,Object> map =  orderService.getOrderInfoByTicket(orderId);
        return Result.ok().data(map);
    }

    @PostMapping("/modifyTicketOrder/{orderId}")
    public Result modifyTicketOrder(@PathVariable String orderId,@RequestBody Params params,HttpServletRequest request){


        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        orderService.modifyTicketOrder(orderId,memberId,params);


        return Result.ok();
    }

    @GetMapping("haveBuy/{movieId}")
    public Result haveBuy(@PathVariable String movieId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == null) {
            return Result.error().data("isbuy", false);
        }
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<MOrder>().eq(MOrder::getMovieId, movieId).eq(MOrder::getMemberId, memberId);
        wrapper.eq(MOrder::getStatus, 1);
        wrapper.eq(MOrder::getSeats,"");
        int count = orderService.count(wrapper);
        if (count > 0) {
            return Result.error().data("isbuy", true);
        }
        return Result.error().data("isbuy", false);
    }

}

