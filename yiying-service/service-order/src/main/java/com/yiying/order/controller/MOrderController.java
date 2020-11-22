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


    @PostMapping("createOrder/{memberId}/{movieId}")
    public Result createOrder(@PathVariable String memberId,@PathVariable String movieId) {

        //根据课程id和用户id创建订单
        String orderId = orderService.createOrder(movieId, memberId);
        return Result.ok().data("orderId", orderId);
    }


    @PostMapping("createOrdersTicket/{memberId}/{movieId}")
    public Result createOrdersTicket(@PathVariable String memberId,@PathVariable String movieId, HttpServletRequest request) {

        //根据课程id和用户id创建订单
        String orderId = orderService.createOrdersTicket(movieId, memberId);
        return Result.ok().data("orderId", orderId);
    }



    @GetMapping("getOrderInfo/{orderId}")
    public Result getMOrderInfo(@PathVariable String orderId) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, orderId);
        MOrder one = orderService.getOne(wrapper);
        if(one.getStatus() == 1){
            try {
                throw new Exception("请不要重新下单，该订单已经被消费！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.error().data("msg", "请不要重复下单！");
        }
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

    @PostMapping("/modifyTicketOrder/{memberId}{orderId}")
    public Result modifyTicketOrder(@PathVariable String memberId,@PathVariable String orderId,@RequestBody Params params){

//
//        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        orderService.modifyTicketOrder(orderId,memberId,params);


        return Result.ok();
    }



}

