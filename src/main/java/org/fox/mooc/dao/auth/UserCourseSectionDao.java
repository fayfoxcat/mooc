package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.UserCourseSection;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-10:49
 */
public interface UserCourseSectionDao {
    /**
     * 根据id获取
     */
    public UserCourseSection getById(Long id);

    /**
     * 根据条件获取指定记录
     * @param userCourseSection
     * @return
     */
    public UserCourseSection getByRecord(UserCourseSection userCourseSection);

    /**
     * 获取所有
     */
    public List<UserCourseSection> queryAll(UserCourseSection queryEntity);

    /**
     * 获取最新的学习记录
     */
    public UserCourseSection queryLatest(UserCourseSection userCourseSection);

    /**
     * 查询已学章节记录数
     */
    public Integer getRecordCount(UserCourseSection userCourseSection);

    /**
     * 用户加入学习课程数
     */
    public Integer getStudyCourseCount(UserCourseSection userCourseSection);

    /**
     * 返回当前用户已学习课程
     */
    public List<UserCourseSectionDTO> getStudyCourse(@Param("userId") Long userId,@Param("rowIndex") Integer rowIndex,@Param("pageSize") Integer pageSize);
    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(UserCourseSection queryEntity);

    /**
     * 分页获取
     */
    public List<UserCourseSectionDTO> queryPage(@Param("userCourseSectionDTO") UserCourseSectionDTO userCourseSectionDTO,@Param("rowIndex") Integer rowIndex,@Param("pageSize") Integer pageSize);

    /**
     * 创建新记录
     */
    public void createSelectivity(UserCourseSection entity);

    /**
     * 根据id更新
     */
    public void update(UserCourseSection entity);

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(UserCourseSection entity);

    /**
     * 移除课程（批量删除所有学习记录）
     */
    public void remove(UserCourseSection userCourseSection);

    /**
     * 物理删除
     */
    public void delete(UserCourseSection entity);

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserCourseSection entity);

}
