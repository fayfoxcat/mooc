package org.fox.mooc.service.auth;

import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserFollows;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-1209
 */
public interface UserFollowsService {
    /**
     * 根据id获取
     */
    public UserFollows getById(Long id ) ;

    /**
     * 获取所有
     */
    public List<UserFollows> queryAll(UserFollows queryEntity ) ;

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(UserFollows userFollows);

    /**
     * 分页获取
     */
    public List<AuthUser> queryPage(Long userId , Integer rowIndex, Integer pageSize);

    /**
     * 分页获取
     */
    //    public TailPage<UserFollowStudyRecord> queryUserFollowStudyRecordPage(UserFollowStudyRecord queryEntity , TailPage<UserFollowStudyRecord> page);

    /**
     * 创建
     */
    public void createSelectivity(UserFollows entity );

    /**
     * 根据id更新
     */
    public void update(UserFollows entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(UserFollows entity );

    /**
     * 物理删除
     */
    public void delete(UserFollows entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserFollows entity );
}
