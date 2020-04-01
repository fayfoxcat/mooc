package org.fox.mooc.service.course.Impl;

import org.fox.mooc.dao.course.CourseSectionDao;
import org.fox.mooc.entity.course.CourseSection;
import org.fox.mooc.service.course.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/21-1605
 */
@Service
public class CourseSectionServiceImpl implements CourseSectionService {
    @Autowired
    private CourseSectionDao courseSectionDao;

    @Override
    public CourseSection getById(Long id)  {
        return courseSectionDao.getById(id);
    }

    @Override
    public List<CourseSection> queryAll( CourseSection courseSection ) {
        return courseSectionDao.queryAll(courseSection);
    }

    /**
     * 获取课程章最大的sort
     */
    @Override
    public Integer getMaxSort(Long courseId){
        return courseSectionDao.getMaxSort(courseId);
    }

    @Override
    public void createSelectivity(CourseSection entity) {
        courseSectionDao.createSelectivity(entity);
    }

    /**
     * 批量创建
     */
    @Override
    public void createList(List<CourseSection> entityList ) {
        courseSectionDao.createList(entityList);
    }

    @Override
    public void update(CourseSection entity ) {
        courseSectionDao.update(entity);
    }

    @Override
    public void updateSelectivity(CourseSection courseSection ) {
        courseSectionDao.updateSelectivity(courseSection);
    }

    @Override
    public void delete(CourseSection courseSection ) {
        courseSectionDao.delete(courseSection);
    }

    @Override
    public void deleteLogic(CourseSection entity ) {
        courseSectionDao.deleteLogic(entity);
    }

    /**
     * 比当前sort大的，正序排序的第一个
     * @param curCourseSection
     * @return
     */
    @Override
    public CourseSection getSortSectionMax(CourseSection curCourseSection )  {
        return courseSectionDao.getSortSectionMax(curCourseSection);
    }

    /**
     * 比当前sort小的，倒序排序的第一个
     * @param curCourseSection
     * @return
     */
    @Override
    public CourseSection getSortSectionMin(CourseSection curCourseSection )  {
        return courseSectionDao.getSortSectionMin(curCourseSection);
    }

}
