package com.yiying.movie.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiying.VodService;
import com.yiying.config.QiException;
import com.yiying.movie.dto.PlayHallSeat;
import com.yiying.movie.entity.*;
import com.yiying.movie.mapper.MMovieMapper;
import com.yiying.movie.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.movie.vo.MovieItemVo;
import com.yiying.movie.vo.MoviePublishVo;
import com.yiying.movie.vo.MovieQuery;
import com.yiying.movie.vo.MovieVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
@Service
public class MMovieServiceImpl extends ServiceImpl<MMovieMapper, MMovie> implements MMovieService {

    @Autowired
    private MMoviePlayHallService moviePlayHallService;

    @Autowired
    private MPlayHallService playHallService;

    @Autowired
    private MSubjectService subjectService;

    @Autowired
    private MMovieVideoService mMovieVideoService;

    @Reference(check = false)
    private VodService vodService;

    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

    @Override
    public MovieVo getMovieInfo(String id) {

        MMovie mMovie = this.getById(id);
        if (mMovie == null) {
            throw new QiException(20002, "数据不存在");
        }
        MovieVo movieVo = new MovieVo();
        BeanUtils.copyProperties(mMovie, movieVo);
        String releaseDate = mMovie.getReleaseDate();
        String date1 = releaseDate.substring(0, 10);
        int runtime = Integer.parseInt(mMovie.getRuntime());

        //设置页面回显的日期
        try {
            Date parse = defaultDateFormat.parse(releaseDate);
            long time = parse.getTime();
            long tim1 = time + runtime;
            Date date = new Date(tim1);
            String da[] = new String[2];
            da[0] = simpleDateFormat.format(parse);
            da[1] = simpleDateFormat.format(date);
            movieVo.setDate(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movieVo;
    }

    /**
     * 保存电影信息
     *
     * @param movieInfo
     */
    @Override
    @Transactional
    public String saveMovieInfo(MovieVo movieInfo) {
        MMovie mMovie = null;
        try {
            mMovie = new MMovie();
            //电影未发布
            mMovie.setStatus("Movie_DRAFT");
            BeanUtils.copyProperties(movieInfo, mMovie);
            String[] date1 = movieInfo.getDate();
//            mMovie.setReleaseDate(date1[0]);//数组得第一个是电影要发布的时候

            String s1 = date1[0];
            String s2 = date1[1];
            System.out.println(s1 + "  " + s2);
            s1 = s1.replace("Z", " UTC");
            s2 = s2.replace("Z", " UTC");

            Date parse = simpleDateFormat.parse(s1);
            mMovie.setReleaseDate(defaultDateFormat.format(parse));
            Date parse1 = simpleDateFormat.parse(s2);

            long time = parse.getTime();
            long time1 = parse1.getTime();
            int runtime = (int) ((time1 - time) / (1000 * 60 * 60));
            //设置播放时间
            mMovie.setRuntime(String.valueOf(runtime));
            mMovie.setOnlinePrice(movieInfo.getPrice().multiply(new BigDecimal(0.7)));
            int isFree = movieInfo.getPrice().intValue() > 0 ? 1 : 0;
            mMovie.setIsFree(isFree);

            //保存电影信息
            boolean save = this.save(mMovie);
            if (!save) {
                throw new QiException(20002, "保存失败");
            }


            MMoviePlayHall moviePlayHall = new MMoviePlayHall();
            Date date = defaultDateFormat.parse(mMovie.getReleaseDate());
            moviePlayHall.setStartUseTime(date);
            Date parse2 = simpleDateFormat.parse(s2);
            String format = defaultDateFormat.format(parse2);
            moviePlayHall.setEndUseTime(defaultDateFormat.parse(format));
            moviePlayHall.setMovieId(mMovie.getMovieId());

            String s = initializeHallSeats(movieInfo.getSeat());
            //初始化座位信息
            moviePlayHall.setSeats(s);

            moviePlayHall.setWatchHallId(movieInfo.getSubjectHallId());

            MPlayHall byId = playHallService.getById(movieInfo.getSubjectHallId());
            moviePlayHall.setChangci(mMovie.getReleaseDate() + " " + byId.getTitle());
            moviePlayHallService.save(moviePlayHall);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mMovie.getMovieId();
    }

    /**
     * 初始化座位信息
     *
     * @return
     * @param seat1
     */
    private String initializeHallSeats(int[][] seat1) {

        ArrayList<PlayHallSeat> seats = new ArrayList<>();
        for (int i = 0; i < seat1.length; i++) {
            int t = i + 1;
            for (int j = 0; j < seat1[i].length; j++) {
                PlayHallSeat seat = new PlayHallSeat();
                seat.setSeatsRow(String.valueOf(t));
                seat.setSeatsColumn(String.valueOf(j + 1));
                seat.setStatus(seat1[i][j]+"");
                seats.add(seat);
            }
        }
        //将list数组转化为json字符串
        return JSON.toJSONString(seats);
    }

    @Override
    public void updateMovieInfo(MovieVo movieInfo) {

        MMovie mMovie = new MMovie();
        BeanUtils.copyProperties(movieInfo, mMovie);
        mMovie.setReleaseDate(movieInfo.getDate() + " " + movieInfo.getDate());
        boolean b = this.updateById(mMovie);


        //保存电影播放厅信息
        MMoviePlayHall playHall = new MMoviePlayHall();

        Date date = null;
        try {
            date = simpleDateFormat.parse(mMovie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        playHall.setStartUseTime(date);
        String runtime = mMovie.getRuntime();
        long time = 0;
        try {
            time = simpleDateFormat.parse(runtime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = date.getTime();
        Long aLong = null;
        aLong = time + time1;

        Date endUseTime = new Date(aLong);
        playHall.setEndUseTime(endUseTime);
        playHall.setMovieId(mMovie.getMovieId());

        String s = initializeHallSeats(movieInfo.getSeat());
        //初始化座位信息
        playHall.setSeats(s);

        playHall.setWatchHallId(movieInfo.getSubjectHallId());

        MPlayHall byId = playHallService.getById(movieInfo.getSubjectHallId());
        playHall.setChangci(mMovie.getReleaseDate() + byId.getTitle());

    }

    @Override
    public void removeMovieInfo(String id) {
        this.removeById(id);
        LambdaQueryWrapper<MMoviePlayHall> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MMoviePlayHall::getMovieId, id);
        moviePlayHallService.remove(wrapper);

    }

    /**
     * 获取发布页面的信息
     *
     * @param id
     * @return
     */
    @Override
    public MoviePublishVo getMoviePublishInfoById(String id) {
        MMovie mMovie = baseMapper.selectById(id);
        MoviePublishVo moviePublishVo = new MoviePublishVo();
        BeanUtils.copyProperties(mMovie, moviePublishVo);

        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getId, mMovie.getSubjectParentId());
        MSubject one = subjectService.getOne(wrapper);
        moviePublishVo.setSubjectLevelOne(one.getGenres());

        LambdaQueryWrapper<MSubject> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MSubject::getId, mMovie.getSubjectId());
        MSubject one1 = subjectService.getOne(wrapper1);
        moviePublishVo.setSubjectLevelTwo(one1.getGenres());

        LambdaQueryWrapper<MSubject> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(MSubject::getId, mMovie.getSubjectYearParentId());
        MSubject one2 = subjectService.getOne(wrapper2);
        moviePublishVo.setSubjectLevelOneYear(one2.getGenres());

        LambdaQueryWrapper<MSubject> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(MSubject::getId, mMovie.getSubjectYearId());
        MSubject one3 = subjectService.getOne(wrapper3);
        moviePublishVo.setSubjectLevelTwoYear(one3.getGenres());

        LambdaQueryWrapper<MSubject> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(MSubject::getId, mMovie.getSubjectGeneresParentId());
        MSubject one4 = subjectService.getOne(wrapper4);
        moviePublishVo.setSubjectLevelOneGenres(one4.getGenres());
        LambdaQueryWrapper<MSubject> wrapper5 = new LambdaQueryWrapper<>();
        wrapper5.eq(MSubject::getId, mMovie.getSubjectGeneresId());
        MSubject one5 = subjectService.getOne(wrapper5);
        moviePublishVo.setSubjectLevelTwoGenres(one5.getGenres());

        return moviePublishVo;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param movieQuery
     * @return
     */
    @Override
    public IPage<MMovie> pageByParam(Long page, Long limit, MovieQuery movieQuery) {

        Page<MMovie> mMoviePage = new Page<>(page, limit);
        LambdaQueryWrapper<MMovie> wrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(movieQuery.getTitle())) {
            wrapper.eq(MMovie::getTitle, movieQuery.getTitle());
        }

        if (!StringUtils.isEmpty(movieQuery.getSubjectId())) {
            wrapper.eq(MMovie::getSubjectId, movieQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(movieQuery.getSubjectParentId())) {
            wrapper.eq(MMovie::getSubjectParentId, movieQuery.getSubjectParentId());
        }

//        wrapper.eq(MMovie::getStatus, "Movie_Realeased");
        IPage<MMovie> mMovieIPage = baseMapper.selectPage(mMoviePage, wrapper);
        return mMovieIPage;
    }

    /**
     * 删除电影
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeByMovieId(String id) {

        LambdaQueryWrapper<MMovieVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MMovieVideo::getMovieId, id);
        MMovieVideo one = mMovieVideoService.getOne(wrapper);
        //远程调用删除远程的视频
        vodService.removeVideo(one.getVideoSourceId());

        //删除video表的记录
        mMovieVideoService.removeById(one.getId());

        //删除电影播放厅记录
        LambdaQueryWrapper<MMoviePlayHall> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MMoviePlayHall::getMovieId, id);
        moviePlayHallService.remove(wrapper1);
        // TODO 删除电影评论


        //删除电影记录
        int i = baseMapper.deleteById(id);
        if (i > 0) {
            return true;
        }


        return false;
    }

    /**
     * 获取前八条数据
     *
     * @return
     */
    @Cacheable(value = "movie",key = "TopEightMovie")
    @Override
    public List<MMovie> getTopEightMovie() {
        LambdaQueryWrapper<MMovie> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MMovie::getReleaseDate);
        wrapper.last("limit 8");
        List<MMovie> list = this.list(wrapper);
        return list;
    }

    /**
     * 获取电影的页面信息
     *
     * @param movieId
     * @return
     */
    @Override
    public MovieItemVo getMovieItemById(String movieId) {
        MMovie mMovie = baseMapper.selectById(movieId);
        MovieItemVo movieItemVo = new MovieItemVo();
        BeanUtils.copyProperties(mMovie, movieItemVo);

        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getId, mMovie.getSubjectParentId());
        MSubject one = subjectService.getOne(wrapper);
        movieItemVo.setSubjectLevelOne(one.getGenres());

        LambdaQueryWrapper<MSubject> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MSubject::getId, mMovie.getSubjectId());
        MSubject one1 = subjectService.getOne(wrapper1);
        movieItemVo.setSubjectLevelTwo(one1.getGenres());

        LambdaQueryWrapper<MSubject> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(MSubject::getId, mMovie.getSubjectYearParentId());
        MSubject one2 = subjectService.getOne(wrapper2);
        movieItemVo.setSubjectLevelOneYear(one2.getGenres());

        LambdaQueryWrapper<MSubject> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(MSubject::getId, mMovie.getSubjectYearId());
        MSubject one3 = subjectService.getOne(wrapper3);
        movieItemVo.setSubjectLevelTwoYear(one3.getGenres());

        LambdaQueryWrapper<MSubject> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(MSubject::getId, mMovie.getSubjectGeneresParentId());
        MSubject one4 = subjectService.getOne(wrapper4);
        movieItemVo.setSubjectLevelOneGenres(one4.getGenres());
        LambdaQueryWrapper<MSubject> wrapper5 = new LambdaQueryWrapper<>();
        wrapper5.eq(MSubject::getId, mMovie.getSubjectGeneresId());
        MSubject one5 = subjectService.getOne(wrapper5);
        movieItemVo.setSubjectLevelTwoGenres(one5.getGenres());

        return movieItemVo;
    }

    /**
     * 更新电影的评分
     *
     * @param movieId
     * @param rate
     */
    @Override
    public void updateMovieMsg(String movieId, Integer rate) {
        MMovie movie = this.getById(movieId);
        Double i = Double.parseDouble(movie.getRating());
        i = i + rate;

        movie.setRating((i / movie.getRatingCount() % 10) + "");
        //评分人数累计
        movie.setRatingCount(movie.getRatingCount() + 1);
        this.update(movie, null);
    }

    public static void main(String[] args) throws ParseException {
        String dateTime = "2020-11-01T04:00:00.000Z";
        dateTime = dateTime.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date time = format.parse(dateTime);
            String result = defaultFormat.format(time);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
