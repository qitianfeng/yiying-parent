package com.yiying.movie.service;

import com.yiying.movie.entity.MSubject;
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
public interface MSubjectService extends IService<MSubject> {

    void importSubject(MultipartFile file, MSubjectService subjectService);

    /**
     * 获取所有电影类别
     * @return
     */
    List<SubjectObjNestVo> getAllSubject();

    /**
     * 删除所有电影类别
     * @return
     */
    Boolean deleteAllSubject();

    /**
     * 批量删除
     * @param ids
     * @return
     */

    Boolean deleteBatchById(List<String> ids);

    /**
     * 获取所有电影类别一级分类
     * @return
     */
    List<String> getAllFirstSubject();

    MSubject getTitleById(String subjectId);
}
