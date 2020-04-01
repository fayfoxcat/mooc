package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.entity.UserStudyCourse;
import org.fox.mooc.vo.UserStudyCourseVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/17-14:30
 */
public interface UserStudyCourseDao {

    /*
     * 根据id返回记录
     */
    public UserStudyCourse getById(Long id);

    /*
     * 根据条件分页返回
     */
    public List<UserStudyCourseVO> queryPage(@Param("studyCourse") UserStudyCourse studyCourse, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    /*
     * 根据条件返回总记录数（userId或courseId）
     */
    public Integer getTotalItemsCount(UserStudyCourse studyCourse);

    /*
     * 选择性创建
     */
    public void createSelectivity(UserStudyCourse studyCourse);

    /*
     *选择性更新
     */
    public void updateSelectivity(UserStudyCourse studyCourse);

    /*
     *物理删除
     */
    public void delete(UserStudyCourse studyCourse);

    /*
     *逻辑删除
     */
    public void deleteLogic(UserStudyCourse studyCourse);
}
