package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.entity.UserCollections;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-10:46
 */
public interface UserCollectionDao {
    /**
     * 根据id获取
     */
    public UserCollections getById(Long id);

    /**
     * 根据userid及classify判断当前课程是否收藏
     */
    public UserCollections isCollection(UserCollections userCollections);

    /**
     * 获取所有√
     */
    public List<UserCollections> queryAll(UserCollections queryEntity);

    /**
     * 获取总数量√
     */
    public Integer getTotalItemsCount(UserCollections userCollections);

    /**
     * 分页获取√
     */
    public List<UserCollections> queryPage(@Param("userCollections") UserCollections userCollections, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    /**
     * 创建新记录
     */
    public void create(UserCollections entity);

    /**
     * 创建新记录√
     */
    public void createSelectivity(UserCollections entity);

    /**
     * 根据id更新
     */
    public void update(UserCollections entity);

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(UserCollections entity );

    /**
     * 物理删除
     */
    public void delete(UserCollections entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserCollections userCollections);
}
