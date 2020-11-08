package com.yiying.banner.service;

import com.yiying.banner.entity.MBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-11-08
 */
public interface MBannerService extends IService<MBanner> {

    /**
     * 查询广告
     * @return
     */
    List<MBanner> getTopThree();

}
