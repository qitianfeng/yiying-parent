package com.yiying.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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


    @PostMapping("createMOrder/{movieId}")
    public Result createMOrder(@PathVariable String movieId, HttpServletRequest request) {

        String jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(jwtToken)) {
            return Result.error().message("请登录后再购买");
        }
        //根据课程id和用户id创建订单
        String MOrderId = orderService.createOrder(movieId, jwtToken);
        return Result.ok().data("MOrderId", MOrderId);
    }

    @GetMapping("getMOrderInfo/{orderId}")
    public Result getMOrderInfo(@PathVariable String MOrderId) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, MOrderId);
        MOrder one = orderService.getOne(wrapper);
        return Result.ok().data("order", one);
    }


    @GetMapping("haveBuy/{movieId}")
    public Result haveBuy(@PathVariable String movieId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == null) {
            return Result.error().data("isbuy", false);
        }
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<MOrder>().eq(MOrder::getMovieId, movieId).eq(MOrder::getMemberId, memberId);
        wrapper.eq(MOrder::getStatus, 1);
        int count = orderService.count(wrapper);
        if (count > 0) {
            return Result.error().data("isbuy", true);
        }
        return Result.error().data("isbuy", false);
    }

}

