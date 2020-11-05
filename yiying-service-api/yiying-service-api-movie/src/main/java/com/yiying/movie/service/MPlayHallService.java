package com.yiying.movie.service;

import com.yiying.movie.entity.MPlayHall;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiying.movie.vo.SubjectObjNestVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-20
 */
public interface MPlayHallService extends IService<MPlayHall> {

    void importSubject(MultipartFile file, MPlayHallService playHallService);

    /**
     * 获取所有展厅
     * @return
     */
    List<SubjectObjNestVo> getAllSubject();
}
