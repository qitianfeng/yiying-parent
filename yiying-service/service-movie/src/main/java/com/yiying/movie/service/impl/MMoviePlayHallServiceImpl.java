package com.yiying.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.movie.entity.MMoviePlayHall;
import com.yiying.movie.mapper.MMoviePlayHallMapper;
import com.yiying.movie.service.MMoviePlayHallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
@Service
public class MMoviePlayHallServiceImpl extends ServiceImpl<MMoviePlayHallMapper, MMoviePlayHall> implements MMoviePlayHallService {

    @Override
    public MMoviePlayHall getPlayHall(String movieId) {
        //获取电影展厅的名称
        LambdaQueryWrapper<MMoviePlayHall> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MMoviePlayHall::getMovieId, movieId);
        MMoviePlayHall playHall = this.getOne(wrapper1);
        return playHall;
    }

    @Override
    public MMoviePlayHall getOneByMovieId(String movieId) {


        LambdaQueryWrapper<MMoviePlayHall> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MMoviePlayHall::getMovieId,movieId);
        return this.getOne(wrapper1);
    }
}
