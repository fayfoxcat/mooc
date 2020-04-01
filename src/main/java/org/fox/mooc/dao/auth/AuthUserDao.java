package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.vo.GraphVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/17-14:30
 */
public interface AuthUserDao {
    /**
     * 根据id获取
     */
    public AuthUser getById(Long id);

    /**
     * 根据account获取
     */
    public AuthUser getByAccount(String account);

    /**
     * 根据username 和 password获取
     * @param authUser
     * @return
     */
    public AuthUser getByUsernameAndPassword(AuthUser authUser);

    /**
     * 获取首页推荐5个讲师
     */
    public List<AuthUser> queryRecomd();

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(AuthUser authUser);

    /**
     * 返回图表所需数据
     */
    public List<GraphVO> queryGraph(AuthUser authUser);

    /**
     * 模糊查询分页
     */
    public List<AuthUser> queryPage(@Param("authUser") AuthUser authUser, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);


    /**
     * 根据courseId查询用户
     */
    public List<AuthUser> byCourseIdQuery(@Param("courseId") Long courseId, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);


    /**
     * 创建新记录
     */
    public void createSelectivity(AuthUser entity );

    /**
     * 根据id更新
     */
    public void update(AuthUser entity);

    /**
     * 根据id选择性更新自动（更新用户信息）
     */
    public void updateSelectivity(AuthUser entity );

    /**
     * 物理删除
     */
    public void delete(AuthUser entity);

    /**
     * 逻辑删除
     */
    public void deleteLogic(AuthUser entity);
}
