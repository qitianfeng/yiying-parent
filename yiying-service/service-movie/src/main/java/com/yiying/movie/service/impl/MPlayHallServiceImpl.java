package com.yiying.movie.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.config.QiException;
import com.yiying.movie.dto.excell.ExcelSubjectData;
import com.yiying.movie.entity.MPlayHall;
import com.yiying.movie.listener.ExcelListenerPlayHall;
import com.yiying.movie.mapper.MPlayHallMapper;
import com.yiying.movie.service.MPlayHallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.movie.vo.SubjectObjNestVo;
import com.yiying.movie.vo.SubjectObjVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
@Service
public class MPlayHallServiceImpl extends ServiceImpl<MPlayHallMapper, MPlayHall> implements MPlayHallService {

    @Override
    public void importSubject(MultipartFile file, MPlayHallService playHallService) {
        try {
            //1 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelListenerPlayHall(playHallService)).sheet().doRead();
        }catch(Exception e) {
            e.printStackTrace();
            throw new QiException(20002,"添加影片类别失败");
        }
    }

    /**
     * 获取所有展厅
     *
     * @return
     */
    @Override
    public List<SubjectObjNestVo> getAllSubject() {

        //创建用于展示的数据结构
        List<SubjectObjNestVo> hallNestVos = new ArrayList<>();

        //查询所有一级分类
        LambdaQueryWrapper<MPlayHall> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MPlayHall::getParentId,"0");
        wrapper.orderByDesc(MPlayHall::getWatchHallId);
        List<MPlayHall> mPlayHallsFirse = baseMapper.selectList(wrapper);

        //查询所有二级分类
        LambdaQueryWrapper<MPlayHall> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.ne(MPlayHall::getParentId,"0");
        wrapper2.orderByDesc(MPlayHall::getWatchHallId);
        List<MPlayHall> mPlayHallsSecond = baseMapper.selectList(wrapper2);

        for (MPlayHall playHall : mPlayHallsFirse) {

            SubjectObjNestVo subjectObjNestVo = new SubjectObjNestVo();
            subjectObjNestVo.setId(playHall.getWatchHallId());
            subjectObjNestVo.setLabel(playHall.getTitle());
            List<SubjectObjVo> children = new ArrayList<>();
            for (MPlayHall mPlayHall : mPlayHallsSecond) {
                if (mPlayHall.getParentId().equals(playHall.getWatchHallId())) {
                    SubjectObjVo subjectObjVo = new SubjectObjVo();
                    subjectObjVo.setId(mPlayHall.getWatchHallId());
                    subjectObjVo.setLabel(mPlayHall.getTitle());
                    children.add(subjectObjVo);
                }
            }
            subjectObjNestVo.setChildren(children);
            hallNestVos.add(subjectObjNestVo);

        }

        return hallNestVos;
    }
}
