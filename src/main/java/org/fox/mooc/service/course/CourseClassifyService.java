package org.fox.mooc.service.course;

import org.fox.mooc.entity.course.CourseClassify;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/11-1532
 */
public interface CourseClassifyService {
    /**
     * 根据ID获取
     */
    public CourseClassify getById(Long id);

    /**
     * 查询所有的课程分类
     */
    public List<CourseClassify> queryAll();

    /**
     * 根据code获取
     */
    public CourseClassify getByCode(String code );

    /**
     * 根据条件动态获取
     */
    public List<CourseClassify> queryByCondition(CourseClassify queryEntity );

    /**
     * 分页获取
     */
    //    public TailPage<CourseClassify> queryPage(CourseClassify queryEntity ,TailPage<CourseClassify> page);

    /**
     * 创建
     */
    public void create(CourseClassify entity );

    /**
     * 创建
     */
    public void createSelectivity(CourseClassify entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(CourseClassify entity );

    /**
     * 物理删除
     */
    public void delete(CourseClassify entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseClassify entity );
}
