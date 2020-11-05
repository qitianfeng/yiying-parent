package com.yiying.order.service;

import com.yiying.order.entity.MOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiying.order.vo.OrderVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
public interface MOrderService extends IService<MOrder> {

    /***
     * 创建订单
     * @param movieId
     * @param jwtToken
     * @return
     */
    String createOrder(String movieId, String jwtToken);

    Boolean haveBuy(String memberId, String movieId);

    OrderVo getByOrderNo(String orderNo);

    /**
     * 根据订单id查询
     * @param out_trade_no
     * @return
     */
    String queryByOutTradeNo(String out_trade_no);

    /**
     * 创建购买电影票的订单
     * @param movieId
     * @param jwtToken
     * @return
     */
    String createOrdersTicket(String movieId, String jwtToken);

    /**
     * 填充购买电影票的信息展示
     * @param orderId
     * @return
     */
    Map<String, Object> getOrderInfoByTicket(String orderId);
}
