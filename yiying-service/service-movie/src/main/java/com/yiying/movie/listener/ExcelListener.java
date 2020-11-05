package com.yiying.movie.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.config.QiException;
import com.yiying.movie.dto.excell.ExcelSubjectData;
import com.yiying.movie.entity.MSubject;
import com.yiying.movie.service.MSubjectService;

import java.util.Map;

//创建读取Excel监听器
public class ExcelListener extends AnalysisEventListener<ExcelSubjectData> {
    public MSubjectService subjectService;
    public ExcelListener(){}

    //构造函数注入
    public ExcelListener(MSubjectService subjectService){
        this.subjectService = subjectService;
    }


    //一行一行去读取excle内容
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if (excelSubjectData == null) {
            throw new QiException(20001,"参数出错");
        }

        MSubject mSubject = this.judgeFirstSubject(subjectService, excelSubjectData.getFirstSubjectName());
        if (mSubject == null) {
            mSubject = new MSubject();
            mSubject.setGenres(excelSubjectData.getFirstSubjectName());
            mSubject.setParentId("0");
            subjectService.save(mSubject);
        }

        String pId = mSubject.getId();
        MSubject mSubject1 = this.judgeSecondSubject(subjectService, excelSubjectData.getSecondSubjectName(), pId);
        if (mSubject1 == null) {
            MSubject existSubject = new MSubject();
            existSubject.setParentId(pId);
            existSubject.setGenres(excelSubjectData.getSecondSubjectName());
            subjectService.save(existSubject);
        }

    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }



    /**
     * 判断一级分类是否存在
     * @param subjectService
     * @param name
     * @return
     */

    private MSubject judgeFirstSubject(MSubjectService  subjectService, String name){
        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getGenres,name);
        wrapper.eq(MSubject::getParentId,"0");
        MSubject subject = subjectService.getOne(wrapper);
        return subject;
    }

    /**
     * 判断二级分类是否存在在一级分类下
     * @param subjectService
     * @param name
     * @param pId
     * @return
     */
    private MSubject judgeSecondSubject(MSubjectService  subjectService,String name,String pId) {
        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getGenres,name);
        wrapper.eq(MSubject::getParentId,pId);
        MSubject subject = subjectService.getOne(wrapper);
        return subject;
    }
    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
