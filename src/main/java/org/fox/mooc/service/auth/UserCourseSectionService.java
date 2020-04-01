package org.fox.mooc.service.auth;

import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.UserCourseSection;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-1210
 */
public interface UserCourseSectionService {
    /**
     * 根据id获取
     */
    public UserCourseSection getById(Long id);

    /**
     * 获取所有
     */
    public List<UserCourseSection> queryAll(UserCourseSection queryEntity ) ;

    /**
     * 获取最新的记录
     */
    public UserCourseSection queryLatest(UserCourseSection queryEntity ) ;

    /**
     * 用户加入学习课程数
     */
    public Integer getStudyCourseCount(UserCourseSection userCourseSection);

    /**
     * 分页获取
     */
    public List<UserCourseSectionDTO> queryPage(UserCourseSectionDTO userCourseSectionDTO, Integer rowIndex, Integer pageSize);

    /*
     *查询当前用户已添加课程
     */
    public List<UserCourseSectionDTO> getStudyCourse(Long userId,Integer rowIndex, Integer pageSize);

    /**
     * 创建
     */
    public void createSelectivity(UserCourseSection userCourseSection );

    /**
     * 根据id更新
     */
    public void update(UserCourseSection entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(UserCourseSection entity );

    /**
     * 移除课程（批量删除所有学习记录）
     */
    public void remove(UserCourseSection userCourseSection);

    /**
     * 物理删除
     */
    public void delete(UserCourseSection entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserCourseSection entity );

}
