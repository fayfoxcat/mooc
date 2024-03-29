package org.fox.mooc.service.course;

import org.fox.mooc.entity.course.CourseSection;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/21-1604
 */
public interface CourseSectionService {

    /**
     * 根据id获取
     */
    public CourseSection getById(Long id);

    /**
     * 获取所有
     */
    public List<CourseSection> queryAll(CourseSection queryEntity );

    /**
     * 获取课程章最大的sort
     */
    public Integer getMaxSort( Long courseId);

    /**
     * 分页获取
     */
    //    public TailPage<CourseSection> queryPage(CourseSection queryEntity ,TailPage<CourseSection> page);

    /**
     * 创建
     */
    public void createSelectivity(CourseSection entity );

    /**
     * 批量创建
     */
    public void createList(List<CourseSection> entityList );

    /**
     * 根据id更新
     */
    public void update(CourseSection entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(CourseSection entity );

    /**
     * 物理删除
     */
    public void delete(CourseSection courseSection );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseSection entity );

    /**
     * 比当前sort大的，正序排序的第一个
     * @param curCourseSection
     * @return
     */
    public CourseSection getSortSectionMax(CourseSection curCourseSection ) ;

    /**
     * 比当前sort小的，倒序排序的第一个
     * @param curCourseSection
     * @return
     */
    public CourseSection getSortSectionMin(CourseSection curCourseSection ) ;
}
