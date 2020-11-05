package com.yiying.movie.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.config.QiException;
import com.yiying.movie.dto.excell.ExcelSubjectData;
import com.yiying.movie.entity.MSubject;
import com.yiying.movie.listener.ExcelListener;
import com.yiying.movie.mapper.MSubjectMapper;
import com.yiying.movie.service.MSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.movie.vo.SubjectObjNestVo;
import com.yiying.movie.vo.SubjectObjVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MSubjectServiceImpl extends ServiceImpl<MSubjectMapper, MSubject> implements MSubjectService {

    /**
     * 添加类别
     * 读取excel表
     *
     * @param file
     */
    @Override
    public void importSubject(MultipartFile file, MSubjectService subjectService) {
        try {
            //1 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
            throw new QiException(20002, "添加电影展厅失败");
        }
    }

    /**
     * 获取所有电影类别
     *
     * @return
     */
    @Override
    public List<SubjectObjNestVo> getAllSubject() {

        List<SubjectObjNestVo> nestVos = new ArrayList<>();

        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getParentId, "0");
        wrapper.orderByDesc(MSubject::getId);
        List<MSubject> mSubjects = baseMapper.selectList(wrapper);


        LambdaQueryWrapper<MSubject> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.ne(MSubject::getParentId, "0");
        wrapper1.orderByDesc(MSubject::getId);
        List<MSubject> mSubjectSeconds = baseMapper.selectList(wrapper1);


        for (MSubject mSubject : mSubjects) {
            SubjectObjNestVo subjectObjNestVo = new SubjectObjNestVo();
            List<SubjectObjVo> subjectObjVos = new ArrayList<>();
            subjectObjNestVo.setId(mSubject.getId());
            subjectObjNestVo.setLabel(mSubject.getGenres());
            for (MSubject mSubjectSecond : mSubjectSeconds) {
                if (mSubject.getId().equals(mSubjectSecond.getParentId())) {
                    SubjectObjVo subjectObjVo = new SubjectObjVo();
                    subjectObjVo.setId(mSubjectSecond.getId());
                    subjectObjVo.setLabel(mSubjectSecond.getGenres());
                    subjectObjVos.add(subjectObjVo);
                }
            }
            subjectObjNestVo.setChildren(subjectObjVos);
            nestVos.add(subjectObjNestVo);
        }
        return nestVos;
    }

    /**
     * 删除所有电影类别
     *
     * @return
     */
    @Override
    public Boolean deleteAllSubject() {

        int delete = baseMapper.delete(null);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteBatchById(List<String> ids) {

        int deleteBatchIds = baseMapper.deleteBatchIds(ids);
        if (deleteBatchIds > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取所有电影类别一级分类
     *
     * @return
     */
    @Override
    public List<String> getAllFirstSubject() {
        LambdaQueryWrapper<MSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MSubject::getParentId,"0");
        List<MSubject> mSubjects = baseMapper.selectList(wrapper);
        List<String> allFirstSubject = new ArrayList<>();
        for (MSubject mSubject : mSubjects) {
            String s = new String();
            s=mSubject.getGenres();
            allFirstSubject.add(s);
        }
        return allFirstSubject;
    }
}
