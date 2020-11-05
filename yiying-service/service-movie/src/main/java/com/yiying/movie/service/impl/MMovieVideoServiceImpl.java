package com.yiying.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.VodService;
import com.yiying.movie.entity.MMovie;
import com.yiying.movie.entity.MMovieVideo;
import com.yiying.movie.mapper.MMovieVideoMapper;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.service.MMovieVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-01
 */
@Service
public class MMovieVideoServiceImpl extends ServiceImpl<MMovieVideoMapper, MMovieVideo> implements MMovieVideoService {

    @Reference
    private VodService vodService;
    @Autowired
    private MMovieService mMovieService;

    /***
     * 删除视频
     * @param videoId
     * @return
     */
    @Override
    public Boolean deleteVideo(String videoId) {

        MMovieVideo mMovieVideo = baseMapper.selectById(videoId);
        vodService.removeVideo(mMovieVideo.getVideoSourceId());
        int i = baseMapper.deleteById(mMovieVideo);
        if (i > 0) {
            return true;
        }
        return false;
    }

    /***
     * 保存视频
     * @param video
     */
    @Override
    public MMovieVideo saveVideo(MMovieVideo video) {
        MMovie byId = mMovieService.getById(video.getMovieId());
        video.setTitle(byId.getTitle());
        boolean save = this.save(video);
        if (save) {
            return video;
        }
        return null;
    }

    @Override
    public MMovieVideo getByMovieId(String movieId) {
        LambdaQueryWrapper<MMovieVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MMovieVideo::getMovieId, movieId);
        MMovieVideo mMovieVideo = baseMapper.selectOne(wrapper);
        return mMovieVideo;
    }
}
