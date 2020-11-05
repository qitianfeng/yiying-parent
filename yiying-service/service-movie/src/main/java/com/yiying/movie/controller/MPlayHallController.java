package com.yiying.movie.controller;


import com.yiying.common.Result;
import com.yiying.movie.service.MPlayHallService;
import com.yiying.movie.vo.SubjectObjNestVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/movie/playHall")
public class MPlayHallController {

    @Autowired
    MPlayHallService playHallService;
    @ApiOperation("添加电影展厅")
    @PostMapping("addHallSubject")
    public Result addlHalSubject(MultipartFile file){
        playHallService.importSubject(file,playHallService);
        return Result.ok();
    }
    @ApiOperation("获取所有电影展厅")
    @GetMapping("getAllHallSubject")
    public Result getAllSubject(){
        List<SubjectObjNestVo> subjectNestVoList =  playHallService.getAllSubject();
        return Result.ok().data("list",subjectNestVoList);
    }

}

