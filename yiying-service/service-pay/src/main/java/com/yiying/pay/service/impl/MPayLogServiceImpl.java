package com.yiying.pay.service.impl;

import com.yiying.pay.entity.MPayLog;
import com.yiying.pay.mapper.MPayLogMapper;
import com.yiying.pay.service.MPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;

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

}
