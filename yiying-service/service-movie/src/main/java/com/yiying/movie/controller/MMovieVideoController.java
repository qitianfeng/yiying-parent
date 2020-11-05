package com.yiying.movie.controller;


import com.yiying.common.Result;
import com.yiying.movie.entity.MMovieVideo;
import com.yiying.movie.service.MMovieVideoService;
import com.yiying.movie.vo.MovieVideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-01
 */
@RestController
@RequestMapping("/movie/movieVideo")
public class MMovieVideoController {


    @Autowired
    private MMovieVideoService videoService;



    @PostMapping("addVideo")
    public Result addVideo(@RequestBody MMovieVideo mMovieVideo){

        MMovieVideo video = videoService.saveVideo(mMovieVideo);
        return Result.ok().data("video",video);
    }
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody MMovieVideo video){
        videoService.updateById(video);
        return Result.ok();
    }

    @DeleteMapping("deleteVideo/{videoId}")
    public Result deleteVideo(@PathVariable String videoId) {
        Boolean flag =  videoService.deleteVideo(videoId);
        if (flag) {
            return Result.ok().message("视频删除成功");
        }
        return Result.error();
    }


    @GetMapping("getVideoById/{movieId}")
    public Result getVideoById(@PathVariable String movieId) {
        MMovieVideo video = videoService.getByMovieId(movieId);
        return Result.ok().data("video", video);
    }



}

