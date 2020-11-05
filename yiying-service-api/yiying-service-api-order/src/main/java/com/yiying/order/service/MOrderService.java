package com.yiying.order.service;

import com.yiying.order.entity.MOrder;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
