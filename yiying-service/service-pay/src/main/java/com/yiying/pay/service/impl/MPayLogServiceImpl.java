package com.yiying.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import com.yiying.pay.entity.MPayLog;
import com.yiying.pay.mapper.MPayLogMapper;
import com.yiying.pay.producer.PayProducer;
import com.yiying.pay.service.MPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.pay.vo.OrderExt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@Service
public class MPayLogServiceImpl extends ServiceImpl<MPayLogMapper, MPayLog> implements MPayLogService {

    @Autowired
    PayProducer payProducer;
    @Reference
    MOrderService orderService;
    @Override
    public void updateOrderStatus(Map<String, String> map, String memberId) {
        //获得订单id
        String out_trade_id = map.get("out_trade_no");
        //获取支付金额
        BigDecimal amount = new BigDecimal(map.get("total_amount"));

        //发送信息更改信息支付状态
        OrderExt orderExt = new OrderExt();
        orderExt.setOrderNo(out_trade_id);

        MOrder order = orderService.getOneById(out_trade_id);
        order.setStatus(1);
        orderService.update(order,null);
        try {
            payProducer.sendAsyncMsgByJsonDelay("topic",orderExt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //记录支付日志
        MPayLog payLog=new MPayLog();
        payLog.setOrderNo(out_trade_id);//支付订单号
        payLog.setPayTime(new Date());
//        payLog.setPayType(1);//支付类型
        payLog.setMemberId(memberId);
        payLog.setTotalFee(new BigDecimal(map.get("total_amount")));
        payLog.setTotalFee(amount.multiply(new BigDecimal(100)));//总金额(分)
        payLog.setTradeStatus("1");//支付状态
        payLog.setTransactionId(map.get("trade_no"));
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
