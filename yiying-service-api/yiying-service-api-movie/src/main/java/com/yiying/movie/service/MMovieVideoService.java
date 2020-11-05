package com.yiying.movie.service;

import com.yiying.movie.entity.MMovieVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-01
 */
public interface MMovieVideoService extends IService<MMovieVideo> {

    /***
     * 删除视频
     * @param videoId
     * @return
     */
    Boolean deleteVideo(String videoId);

    /***
     * 保存视频
     * @param video
     */
    MMovieVideo saveVideo(MMovieVideo video);


    MMovieVideo getByMovieId(String movieId);
}
