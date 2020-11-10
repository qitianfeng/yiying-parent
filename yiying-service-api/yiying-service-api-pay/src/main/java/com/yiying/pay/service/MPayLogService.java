package com.yiying.pay.service;

import com.yiying.pay.entity.MPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
public interface MPayLogService extends IService<MPayLog> {

    void updateOrderStatus(Map<String, String> map, String memberId);
}
