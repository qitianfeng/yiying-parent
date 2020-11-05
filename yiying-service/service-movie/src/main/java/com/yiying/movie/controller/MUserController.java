package com.yiying.movie.controller;

import com.yiying.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movie")
public class MUserController {

    @PostMapping("/user/login")
    public Result login(){

        return Result.ok().data("token","admin");
    }

    @GetMapping("user/info")
    public Result getInfo(){
        return Result.ok().data("roles","[admin]").data("name","admin").data("avatar","");
    }

}
