package com.yiying.movie.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yiying.common.Result;
import com.yiying.movie.entity.MMovie;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.vo.MoviePublishVo;
import com.yiying.movie.vo.MovieQuery;
import com.yiying.movie.vo.MovieVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/movie")
public class MMovieController {

    @Autowired
    private MMovieService movieService;


    @GetMapping("/movieInfo/{id}")
    public Result getMovieInfo(@PathVariable("id") String id) {

        MovieVo movieVo = movieService.getMovieInfo(id);
        return Result.ok().data("movieVo", movieVo);
    }

    @PostMapping("saveMovieInfo")
    public Result saveMovieInfo(@RequestBody MovieVo movieInfo) {

        String movieId = movieService.saveMovieInfo(movieInfo);
        return Result.ok().data("movieId", movieId);
    }

    @PostMapping("/updateMovieInfo")
    public Result updateMovieInfo(MovieVo movieInfo) {

        movieService.updateMovieInfo(movieInfo);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    public Result removeMovieInfo(@PathVariable("id") String id) {

        movieService.removeMovieInfo(id);
        return Result.ok();
    }


    /**
     * 发布电影
     * @param id
     */
    @PutMapping("publishMovie/{id}")
    public Result publishMovie(@PathVariable("id") String id) {

        MMovie byId = movieService.getById(id);
        //修改为发布状态
        byId.setStatus("Movie_Realeased");
        movieService.updateById(byId);
        return Result.ok();
    }

    @GetMapping("getMoviePublishInfoById/{id}")
    public Result getMoviePublishInfoById(@PathVariable("id") String id) {
        MoviePublishVo moviePublishVo= movieService.getMoviePublishInfoById(id);
        return Result.ok().data("publish",moviePublishVo);
    }

    @PostMapping("{page}/{limit}")
    public Result pageQueryParam(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) MovieQuery courseQuery){
        IPage<MMovie> mMovieIPage = movieService.pageByParam(page, limit, courseQuery);
        long total = mMovieIPage.getTotal();
        List<MMovie> records = mMovieIPage.getRecords();
        return Result.ok().data("total",total).data("rows",records);

    }

    @DeleteMapping("deleteMovie/{id}")
    public Result removeByCourceId(@PathVariable String id){
        boolean i = movieService.removeByMovieId(id);
        if (i){
            return Result.ok();
        }
        return Result.error();
    }

    //电影评分
    @GetMapping("/rate/{movieId}")
    public Result rateMovie(@PathVariable String movieId, Integer rate){

        movieService.updateMovieMsg(movieId,rate);

        return Result.ok().data("flag",1);
    }
}

