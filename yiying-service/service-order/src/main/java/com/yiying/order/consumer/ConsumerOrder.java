package com.yiying.order.consumer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import com.yiying.pay.vo.OrderExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "topic",consumerGroup = "${rocketmq.consumer.group}")
public class ConsumerOrder implements RocketMQListener<String> {

    @Autowired
    MOrderService orderService;
    @Override
    public void onMessage(String s) {

        OrderExt orderExt = JSON.parseObject(s, OrderExt.class);
        //获得订单id
        String orderNo = orderExt.getOrderNo();
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId,orderNo);
        MOrder order = orderService.getOne(wrapper);
        if(order.getStatus().intValue() == 1)
            return;
        order.setStatus(1);
        //更新支付状态
        orderService.updateById(order);
    }

}
