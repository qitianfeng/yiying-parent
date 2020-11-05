package com.yiying.movie.service.impl;

import com.yiying.movie.entity.MMovieComment;
import com.yiying.movie.mapper.MMovieCommentMapper;
import com.yiying.movie.service.MMovieCommentService;
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
public class MMovieCommentServiceImpl extends ServiceImpl<MMovieCommentMapper, MMovieComment> implements MMovieCommentService {

}
