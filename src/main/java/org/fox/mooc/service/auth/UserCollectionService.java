package org.fox.mooc.service.auth;

import org.fox.mooc.entity.UserCollections;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-1211
 */
public interface UserCollectionService {
    /**
     * 根据id获取
     */
    public UserCollections getById(Long id);

    /**
     * 根据userid及classify判断当前课程是否收藏
     */
    public UserCollections isCollection(UserCollections userCollections);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(UserCollections userCollections );


    /**
     * 获取所有
     */
    public List<UserCollections> queryAll(UserCollections queryEntity );

    /**
     * 分页获取
     */
    public List<UserCollections> queryPage(UserCollections userCollections , Integer rowIndex, Integer pageSize);

    /**
     * 创建
     */
    public void createSelectivity(UserCollections entity );

    /**
     * 根据id更新
     */
    public void update(UserCollections entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(UserCollections userCollections );

    /**
     * 物理删除
     */
    public void delete(UserCollections entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserCollections entity );
}
