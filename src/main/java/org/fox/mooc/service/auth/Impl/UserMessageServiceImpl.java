package org.fox.mooc.service.auth.Impl;

import org.fox.mooc.dao.auth.UserMessagesDao;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.service.auth.UserMessageService;
import org.fox.mooc.vo.UserMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/29-12:29
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessagesDao userMessagesDao;

    @Override
    public UserMessage getById(Long id) {
        return null;
    }

    @Override
    public List<UserMessage> getByUserId(Long userId) {
        return userMessagesDao.getByUserId(userId);
    }

    @Override
    public List<UserMessageVO> queryPage(UserMessage userMessage, Integer rowIndex, Integer pageSize) {
        return userMessagesDao.queryPage(userMessage,rowIndex,pageSize);
    }

    @Override
    public List<UserMessageVO> queryApplyPage(UserMessage userMessage, Integer rowIndex, Integer pageSize) {
        return userMessagesDao.queryApplyPage(userMessage,rowIndex,pageSize);
    }


    @Override
    public List<UserMessage> queryAll(UserMessage queryEntity) {
        return null;
    }

    @Override
    public Integer getTotalItemsCount(UserMessage queryEntity) {
        return userMessagesDao.getTotalItemsCount(queryEntity);
    }

    @Override
    public void create(UserMessage entity) {

    }

    @Override
    public void createSelectivity(UserMessage userMessage) {
        userMessage.setStatus(0);
        userMessage.setDel(0);
        userMessage.setCreateTime(new Date());
        userMessage.setUpdateTime(new Date());
        userMessagesDao.createSelectivity(userMessage);
    }

    @Override
    public void update(UserMessage entity) {

    }

    @Override
    public void updateSelectivity(UserMessage userMessage) {
        userMessagesDao.updateSelectivity(userMessage);
    }

    @Override
    public void delete(UserMessage entity) {

    }

    @Override
    public void deleteLogic(UserMessage entity) {

    }
}
