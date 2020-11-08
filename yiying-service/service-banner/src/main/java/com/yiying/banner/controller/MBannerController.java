package com.yiying.banner.controller;


import com.yiying.banner.entity.MBanner;
import com.yiying.banner.service.MBannerService;
import com.yiying.banner.vo.MBannerVo;
import com.yiying.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qitianfeng
 * @since 2020-11-08
 */
@RestController
@RequestMapping("/banner")
public class MBannerController {

    @Autowired
    private MBannerService bannerService;

    //查询前三条广告
    @GetMapping("getTopThree")
    public Result getTopThree(){
        List<MBanner>  bannerVoList= bannerService.getTopThree();
        return Result.ok().data("banner",bannerVoList);
    }

}

