package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.entity.UserFollowStudyRecord;
import org.fox.mooc.entity.UserFollows;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-10:47
 */
public interface UserFollowsDao {
    /**
     * 根据id获取
     */
    public UserFollows getById(Long id);

    /**
     * 获取所有
     */
    public List<UserFollows> queryAll(UserFollows userFollows);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(UserFollows userFollows);

    /**
     * 分页获取
     */
    public List<UserFollows> queryPage(@Param("userId") Long userId, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    /**
     * 获取总数量
     */
    public Integer getFollowStudyRecordCount(UserFollowStudyRecord queryEntity);

    /**
     * 分页获取
     */
    //    public List<UserFollowStudyRecord> queryFollowStudyRecord(UserFollowStudyRecord queryEntity , TailPage<UserFollowStudyRecord> page);

    /**
     * 创建新记录
     */
    public void createSelectivity(UserFollows entity);

    /**
     * 根据id更新
     */
    public void update(UserFollows entity);

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(UserFollows entity);

    /**
     * 物理删除
     */
    public void delete(UserFollows entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserFollows entity);
}
