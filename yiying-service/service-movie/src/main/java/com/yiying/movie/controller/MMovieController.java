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

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/movie")
public class MMovieController {

    @Autowired
    private MMovieService mMovieService;


    @GetMapping("/movieInfo/{id}")
    public Result getMovieInfo(@PathVariable("id") String id) {

        MovieVo movieVo = mMovieService.getMovieInfo(id);
        return Result.ok().data("movieVo", movieVo);
    }

    @PostMapping("saveMovieInfo")
    public Result saveMovieInfo(@RequestBody MovieVo movieInfo) {

        String movieId = mMovieService.saveMovieInfo(movieInfo);
        return Result.ok().data("movieId", movieId);
    }

    @PostMapping("/updateMovieInfo")
    public Result updateMovieInfo(MovieVo movieInfo) {

        mMovieService.updateMovieInfo(movieInfo);
        return Result.ok();
    }

    @DeleteMapping("/remove/{id}")
    public Result removeMovieInfo(@PathVariable("id") String id) {

        mMovieService.removeMovieInfo(id);
        return Result.ok();
    }


    /**
     * 发布电影
     * @param id
     */
    @PutMapping("publishMovie/{id}")
    public Result publishMovie(@PathVariable("id") String id) {

        MMovie byId = mMovieService.getById(id);
        //修改为发布状态
        byId.setStatus("Movie_Realeased");
        mMovieService.updateById(byId);
        return Result.ok();
    }

    @GetMapping("getMoviePublishInfoById/{id}")
    public Result getMoviePublishInfoById(@PathVariable("id") String id) {
        MoviePublishVo moviePublishVo= mMovieService.getMoviePublishInfoById(id);
        return Result.ok().data("publish",moviePublishVo);
    }

    @PostMapping("{page}/{limit}")
    public Result pageQueryParam(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) MovieQuery courseQuery){
        IPage<MMovie> mMovieIPage = mMovieService.pageByParam(page, limit, courseQuery);
        long total = mMovieIPage.getTotal();
        List<MMovie> records = mMovieIPage.getRecords();
        return Result.ok().data("total",total).data("rows",records);

    }

    @DeleteMapping("deleteMovie/{id}")
    public Result removeByCourceId(@PathVariable String id){
        boolean i = mMovieService.removeByMovieId(id);
        if (i){
            return Result.ok();
        }
        return Result.error();
    }

    //电影评分
    @GetMapping("/rate/{movieId}")
    public Result rateMovie(@PathVariable String movieId, Integer rate){

        mMovieService.updateMovieMsg(movieId,rate);

        return Result.ok().data("flag",1);
    }
}

