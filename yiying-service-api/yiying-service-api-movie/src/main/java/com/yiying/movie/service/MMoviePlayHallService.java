package com.yiying.movie.service;

import com.yiying.movie.entity.MMoviePlayHall;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
public interface MMoviePlayHallService extends IService<MMoviePlayHall> {

    //获取电影展厅的名称
    MMoviePlayHall getPlayHall(String movieId);
}
