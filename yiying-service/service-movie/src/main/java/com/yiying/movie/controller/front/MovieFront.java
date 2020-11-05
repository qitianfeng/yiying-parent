package com.yiying.movie.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.movie.entity.MMovie;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.vo.MovieItemVo;
import com.yiying.movie.vo.MovieQuery;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieFront {

    @Autowired
    private MMovieService mMovieService;




  @GetMapping("/index")
    public Result index(){
      List<MMovie> mMovies = mMovieService.getTopEightMovie();
      return Result.ok().data("list",mMovies);
  }

    @ApiOperation("电影详情页")
    @GetMapping("getMovie/{movieId}")
    public Result getCourseByTeacherId(@PathVariable String movieId, HttpServletRequest request) {

        String token = JwtUtils.getMemberIdByJwtToken(request);
        Boolean haveBuy = false;

        MovieItemVo movie = mMovieService.getMovieItemById(movieId);

        return  Result.ok().data("movie",movie);
    }

}
