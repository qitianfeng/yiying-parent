package com.yiying.movie.controller;


import com.alibaba.fastjson.JSON;
import com.yiying.common.Result;
import com.yiying.movie.service.MSubjectService;
import com.yiying.movie.vo.SubjectObjNestVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
@RequestMapping("/movie/subject")
public class MSubjectController {

    @Autowired
    private MSubjectService subjectService;

    @PostMapping("addGenresSubject")
    public Result addGenresSubject(MultipartFile file) {
        subjectService.importSubject(file, subjectService);
        return Result.ok();
    }

    @ApiOperation("获取所有电影类别")
    @GetMapping("getAllSubject")
    public Result getAllSubject() {
        List<SubjectObjNestVo> subjectNestVoList = subjectService.getAllSubject();
        return Result.ok().data("list", subjectNestVoList);
    }

    @ApiOperation("获取所有电影一級类别")
    @GetMapping("getAllFirstSubject")
    public Result getAllFirstSubject() {
        List<String> subjectNestVoList = subjectService.getAllFirstSubject();
        return Result.ok().data("list", subjectNestVoList);
    }

    @ApiOperation("删除所有电影类别")
    @DeleteMapping("deleteAllSubject")
    public Result deleteAllSubject() {
        Boolean flag = subjectService.deleteAllSubject();
        if (flag) {
            return Result.ok().data("message", "删除成功");
        }
        return Result.error().data("message", "删除失败");
    }

    @ApiOperation("批量删除所有电影类别")
    @DeleteMapping("deleteBatchSubject")
    public Result deleteBatchSubject(@RequestBody  String ids) {
        List<String> list = JSON.parseObject(ids, List.class);
        Boolean flag = subjectService.deleteBatchById(list);
        if (flag) {
            return Result.ok().data("message", "删除成功");
        }
        return Result.error().data("message", "删除失败");
    }

    public static void main(String[] args) {
        List list = JSON.parseObject("[1]", List.class);
        System.out.println(list);
    }
}

