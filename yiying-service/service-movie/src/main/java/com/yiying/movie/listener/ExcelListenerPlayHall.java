package com.yiying.movie.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.config.QiException;
import com.yiying.movie.dto.PlayHallSeat;
import com.yiying.movie.dto.excell.ExcelSubjectData;
import com.yiying.movie.service.MPlayHallService;
import com.yiying.movie.entity.MPlayHall;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ExcelListenerPlayHall extends AnalysisEventListener<ExcelSubjectData> {

    final static Logger logger = LoggerFactory.getLogger(ExcelListenerPlayHall.class);
    public MPlayHallService playHallService;

    public ExcelListenerPlayHall() {
    }

    public ExcelListenerPlayHall(MPlayHallService playHallService) {
        this.playHallService = playHallService;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {

        if (excelSubjectData == null) {
            throw new QiException(20001, "参数出错");
        }

        //添加一级分类
        MPlayHall playHall = this.judgeFirstSubject(playHallService, excelSubjectData.getFirstSubjectName());
        if (playHall == null) {
            playHall = new MPlayHall();
            String name = excelSubjectData.getFirstSubjectName();
            logger.info(name);
            playHall.setTitle(name);
            playHall.setParentId("0");

        /*    //初始化座位信息
            String hallSeats = initializeHallSeats();
            playHall.setSeat(hallSeats);
*/
            playHallService.save(playHall);
        }

        //获取一级分类Id
        String watchHallId = playHall.getWatchHallId();

        MPlayHall playHall2 = this.judgeSecondSubject(playHallService, excelSubjectData.getSecondSubjectName(), watchHallId);
        if (playHall2 == null) {
            MPlayHall existTwoSubject = new MPlayHall();
            existTwoSubject.setTitle(excelSubjectData.getSecondSubjectName());
            existTwoSubject.setParentId(watchHallId);
            playHallService.save(existTwoSubject);
        }


    }



    /**
     * 判断一级分类是否存在
     *
     * @param playHallService
     * @param name
     * @return
     */

    private MPlayHall judgeFirstSubject(MPlayHallService playHallService, String name) {
        LambdaQueryWrapper<MPlayHall> wrapper = new LambdaQueryWrapper<MPlayHall>();
        wrapper.eq(MPlayHall::getTitle, name);
        wrapper.eq(MPlayHall::getParentId, "0");
        MPlayHall playHall = (MPlayHall) playHallService.getOne(wrapper);
        return playHall;
    }

    /**
     * 判断二级分类是否存在在一级分类下
     *
     * @param playHallService
     * @param name
     * @param pId
     * @return
     */
    private MPlayHall judgeSecondSubject(MPlayHallService playHallService, String name, String pId) {
        LambdaQueryWrapper<MPlayHall> wrapper = new LambdaQueryWrapper<MPlayHall>();
        wrapper.eq(MPlayHall::getTitle, name);
        wrapper.eq(MPlayHall::getParentId, pId);
        MPlayHall playHall = (MPlayHall) playHallService.getOne(wrapper);
        return playHall;
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
