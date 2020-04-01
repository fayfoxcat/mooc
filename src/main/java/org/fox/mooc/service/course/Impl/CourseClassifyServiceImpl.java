package org.fox.mooc.service.course.Impl;

import org.apache.commons.collections4.CollectionUtils;
import org.fox.mooc.dao.course.CourseClassifyDao;
import org.fox.mooc.entity.course.CourseClassify;
import org.fox.mooc.service.course.CourseClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/11-1533
 */
@Service
public class CourseClassifyServiceImpl implements CourseClassifyService {

    @Autowired
    private CourseClassifyDao courseClassifyDao;

    @Override
    public CourseClassify getById(Long id){
        return null;
    }

    @Override
    public List<CourseClassify> queryAll()  {
        return courseClassifyDao.queryAll();
    }

    /**
     * 根据code返回其下级分类
     */
    @Override
    public CourseClassify getByCode(String code ){
        //判空
        if (StringUtils.isEmpty(code)) {
            return null;
        } else {
            CourseClassify courseClassify = new  CourseClassify();
            courseClassify.setCode(code);
            List<CourseClassify> list = courseClassifyDao.queryByCondition(courseClassify);
            //下级分类是否为空,非空返回list中第一个数据
            if (CollectionUtils.isNotEmpty(list))
                return list.get(0);
        }
        return null;
    }

    /*
    * 根据code返回同级分类
    */
    @Override
    public List<CourseClassify> queryByCondition(CourseClassify queryEntity ) {
        return courseClassifyDao.queryByCondition(queryEntity);
    }

    @Override
    public void create(CourseClassify entity ) {

    }

    @Override
    public void createSelectivity(CourseClassify courseClassify ) {
        courseClassify.setCreateTime(new Date());
        courseClassifyDao.createSelectivity(courseClassify);
    }

    @Override
    public void updateSelectivity(CourseClassify courseClassify ) {
        courseClassifyDao.updateSelectivity(courseClassify);
    }

    @Override
    public void delete(CourseClassify courseClassify ) {
        courseClassifyDao.delete(courseClassify);
    }

    @Override
    public void deleteLogic(CourseClassify entity ) {

    }
}
