package com.yiying.movie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yiying.movie.entity.MMovie;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiying.movie.vo.MovieItemVo;
import com.yiying.movie.vo.MoviePublishVo;
import com.yiying.movie.vo.MovieQuery;
import com.yiying.movie.vo.MovieVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
public interface MMovieService extends IService<MMovie> {

    MovieVo getMovieInfo(String id);

    /**
     * 保存电影信息
     * @param movieInfo
     */

    String saveMovieInfo(MovieVo movieInfo);

    void updateMovieInfo(MovieVo movieInfo);


    void removeMovieInfo(String id);

    /**
     * 获取发布页面的信息
     * @param id
     * @return
     */
    MoviePublishVo getMoviePublishInfoById(String id);

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param courseQuery
     * @return
     */
    IPage<MMovie> pageByParam(Long page, Long limit, MovieQuery courseQuery);

    /**
     * 删除电影
     * @param id
     * @return
     */
    boolean removeByMovieId(String id);

    /**
     * 获取前八条数据
     * @return
     */
    List<MMovie> getTopEightMovie();

    /**
     * 获取电影的页面信息
     * @param movieId
     * @return
     */
    MovieItemVo getMovieItemById(String movieId);

    /**
     * 更新电影的评分
     * @param movieId
     * @param rate
     */
    void updateMovieMsg(String movieId, Integer rate);
}
