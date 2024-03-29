package org.fox.mooc.dao.course;

import org.fox.mooc.entity.course.CourseSection;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/20-956
 */
public interface CourseSectionDao {
    /**
     * 根据id获取
     */
    public CourseSection getById(Long id);

    /**
     * 获取所有
     */
    public List<CourseSection> queryAll(CourseSection queryEntity) ;

    /**
     *
     */
    public Integer getMaxSort(Long courseId );

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(CourseSection queryEntity );

    /**
     * 获取同一课程总章节数目（小节）
     * @param courseId
     * @return
     */
    public Integer byIdCount(Long courseId);
    /**
     * 分页获取
     */
    //    public List<CourseSection> queryPage(CourseSection queryEntity , TailPage<CourseSection> page);

    /**
     * 创建新记录
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
     * 根据id选择性更新自动
     */
    public void updateSelectivity(CourseSection entity );

    /**
     * 物理删除
     */
    public void delete(CourseSection entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseSection entity );

    /**
     * 物理删除课程对应的章节
     */
    public void deleteByCourseId(CourseSection entity );

    /**
     * 逻辑删除课程对应的章节
     */
    public void deleteLogicByCourseId(CourseSection entity );

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
