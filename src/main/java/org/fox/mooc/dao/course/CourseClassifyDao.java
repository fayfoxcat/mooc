package org.fox.mooc.dao.course;

import org.fox.mooc.entity.course.CourseClassify;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/11-1058
 */
public interface CourseClassifyDao {

    /**
     * 根据id获取
     */
    public CourseClassify getById(Long id) ;

    /**
     * 根据code获取
     */
    public CourseClassify getByCode(String code );

    /**
     * 获取所有
     */
    public List<CourseClassify> queryAll() ;

    /**
     * 根据条件动态获取
     * @param queryEntity
     * @return
     */
    public List<CourseClassify> queryByCondition(CourseClassify queryEntity );

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(CourseClassify queryEntity );

    /**
     * 分页获取
     */
    //    public List<CourseClassify> queryPage(CourseClassify queryEntity , TailPage<CourseClassify> page);

    /**
     * 创建新记录
     */
    public void create(CourseClassify entity );

    /**
     * 创建新记录
     */
    public void createSelectivity(CourseClassify entity );

    /**
     * 根据id更新
     */
    public void update(CourseClassify entity );

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(CourseClassify courseClassify );

    /**
     * 物理删除
     */
    public void delete(CourseClassify entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseClassify entity );
}
