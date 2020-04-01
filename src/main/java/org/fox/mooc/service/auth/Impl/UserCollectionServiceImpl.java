package org.fox.mooc.service.auth.Impl;

import org.fox.mooc.dao.auth.UserCollectionDao;
import org.fox.mooc.entity.UserCollections;
import org.fox.mooc.service.auth.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/29-1212
 */
@Service
public class UserCollectionServiceImpl implements UserCollectionService {

    @Autowired
    private UserCollectionDao userCollectionDao;

    @Override
    public UserCollections getById(Long id ){
        return null;
    }

    @Override
    public UserCollections isCollection(UserCollections userCollections )  {
        return userCollectionDao.isCollection(userCollections);
    }

    /**
     * 获取收藏总数量
     * @param userCollections
     * @return
     */
    @Override
    public Integer getTotalItemsCount(UserCollections userCollections ){
        return userCollectionDao.getTotalItemsCount(userCollections);
    }

    /**
     * 根据条件获取所有收藏
     * @param userCollections
     * @return
     */
    @Override
    public List<UserCollections> queryAll(UserCollections userCollections )  {
        return userCollectionDao.queryAll(userCollections);
    }

    /**
     * 分页获取收藏
     * @param userCollections
     * @param rowIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<UserCollections> queryPage(UserCollections userCollections , Integer rowIndex, Integer pageSize){
        return userCollectionDao.queryPage(userCollections, rowIndex, pageSize);
    }

    /**
     * 用户新建收藏
     * @param userCollections
     */
    @Override
    public void createSelectivity(UserCollections userCollections ) {
        userCollections.setCreateTime(new Date());
        userCollectionDao.createSelectivity(userCollections);
    }

    @Override
    public void update(UserCollections entity ) {

    }

    /**
     * 选择性更新
     * @param userCollections
     */
    @Override
    public void updateSelectivity(UserCollections userCollections ) {
        userCollectionDao.updateSelectivity(userCollections);
    }

    @Override
    public void delete(UserCollections entity ) {

    }

    /**
     * 用户取消收藏、逻辑删除
     * @param userCollections
     */
    @Override
    public void deleteLogic(UserCollections userCollections ) {
        userCollectionDao.deleteLogic(userCollections);
    }
}
