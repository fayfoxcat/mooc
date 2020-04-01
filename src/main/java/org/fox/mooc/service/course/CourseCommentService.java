package org.fox.mooc.service.course;

import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.course.CourseComment;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/21-1608
 */
public interface CourseCommentService {
    /**
     * 根据id获取
     */
    public CourseComment getById(Long id) ;

    /**
     * 获取所有
     */
    public List<CourseComment> queryAll(CourseComment queryEntity ) ;

    /**
     * 返回符合条件的评论总数
     * @param courseComment
     * @return
     */
    public Integer getTotalItemsCount(CourseComment courseComment );

    /**
     * 分页获取
     */
    public List<CourseComment> queryPage(CourseComment courseComment , Integer rowIndex , Integer pageSize) ;

    /**
     * 分页获取我的所有课程的qa
     */
    //    public TailPage<CourseComment> queryMyQAItemsPage(CourseComment queryEntity ,TailPage<CourseComment> page);

    /**
     * 创建
     */
    public void create(CourseComment entity );

    /**
     * 创建
     */
    public void createSelectivity(CourseComment entity , AuthUser authUser);

    /**
     * 根据id更新
     */
    public void  update(CourseComment entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(CourseComment entity);

    /**
     * 物理删除
     */
    public void delete(CourseComment courseComment );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseComment entity );
}
