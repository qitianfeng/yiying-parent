package com.yiying.banner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.banner.entity.MBanner;
import com.yiying.banner.mapper.MBannerMapper;
import com.yiying.banner.service.MBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-11-08
 */
@Service
public class MBannerServiceImpl extends ServiceImpl<MBannerMapper, MBanner> implements MBannerService {

    /**
     * 查询广告
     *
     * @return
     */
    @Override
    public List<MBanner> getTopThree() {
        LambdaQueryWrapper<MBanner> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("limit 3");
        List<MBanner> list = this.list(wrapper);

        return list;
    }
}
