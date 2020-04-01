package org.fox.mooc.service.auth.Impl;
import org.fox.mooc.dao.auth.AuthUserDao;
import org.fox.mooc.dao.auth.UserFollowsDao;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserFollows;
import org.fox.mooc.service.auth.UserFollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/29-12:20
 */
@Service
public class UserFollowsServiceImpl implements UserFollowsService {

    @Autowired
    private UserFollowsDao userFollowsDao;

    @Autowired
    private AuthUserDao authUserDao;

    @Override
    public UserFollows getById(Long id) {
        return userFollowsDao.getById(id);
    }

    @Override
    public List<UserFollows> queryAll(UserFollows userFollows) {
        return userFollowsDao.queryAll(userFollows);
    }

    @Override
    public Integer getTotalItemsCount(UserFollows userFollows) {
        return userFollowsDao.getTotalItemsCount(userFollows);
    }

    @Override
    public List<AuthUser> queryPage(Long userId, Integer rowIndex, Integer pageSize) {
        List<UserFollows> follows = userFollowsDao.queryPage(userId, rowIndex, pageSize);
        List<AuthUser> authUsers = new ArrayList<>();
        for (UserFollows follow : follows) {
            AuthUser au = authUserDao.getById(follow.getFollowId());
            authUsers.add(au);
        }
        return authUsers;
    }

    @Override
    public void createSelectivity(UserFollows userFollows) {
        userFollowsDao.createSelectivity(userFollows);
    }

    @Override
    public void update(UserFollows userFollows) {
        userFollowsDao.update(userFollows);
    }

    @Override
    public void updateSelectivity(UserFollows userFollows) {
        userFollowsDao.updateSelectivity(userFollows);
    }

    @Override
    public void delete(UserFollows userFollows) {
        userFollowsDao.delete(userFollows);
    }

    @Override
    public void deleteLogic(UserFollows userFollows) {
        userFollowsDao.deleteLogic(userFollows);
    }
}
