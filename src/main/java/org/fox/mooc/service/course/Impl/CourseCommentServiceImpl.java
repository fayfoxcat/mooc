package org.fox.mooc.service.course.Impl;

import org.fox.mooc.dao.auth.AuthUserDao;
import org.fox.mooc.dao.auth.UserMessagesDao;
import org.fox.mooc.dao.course.CourseCommentDao;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.entity.course.CourseComment;
import org.fox.mooc.service.course.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/21-1608
 */
@Service
public class CourseCommentServiceImpl implements CourseCommentService {
    @Autowired
    private AuthUserDao authUserDao;

    @Autowired
    private CourseCommentDao courseCommentDao;

    @Autowired
    private UserMessagesDao userMessagesDao;

    @Override
    public CourseComment getById(Long id)  {
        return courseCommentDao.getById(id);
    }

    @Override 
    public List<CourseComment> queryAll(CourseComment queryEntity ){
        return courseCommentDao.queryAll(queryEntity);
    }

    @Override 
    public Integer getTotalItemsCount(CourseComment courseComment ){
        return courseCommentDao.getTotalItemsCount(courseComment);
    }

    @Override 
    public List<CourseComment>  queryPage(CourseComment courseComment , Integer rowIndex , Integer pageSize ) {
        return courseCommentDao.queryPage(courseComment, rowIndex, pageSize);
    }


    @Override 
    public void create(CourseComment entity ) {
        courseCommentDao.create(entity);
    }

    /**
     * 选择性创建新评论
     */
    @Override
    @Transactional
    public void createSelectivity(CourseComment courseComment, AuthUser authUser) {
        courseComment.setCreateTime(new Date());
        courseComment.setUpdateTime(new Date());
        courseComment.setCreateUser(courseComment.getAccount());
        courseComment.setUpdateUser(courseComment.getAccount());
        courseCommentDao.createSelectivity(courseComment);
        /*过滤自己对自己的消息*/
        if (!courseComment.getToAccount().equals(authUser.getAccount())) {
            if (courseComment.getId() > 0) {
                UserMessage userMessage = new UserMessage();
                userMessage.setUserId(authUserDao.getByAccount(courseComment.getToAccount()).getId());
                userMessage.setSendUserId(authUser.getId());
                userMessage.setSendUserName(authUser.getUsername());
                userMessage.setRefId(courseComment.getId().toString());
                userMessage.setContent(courseComment.getContent());
                userMessage.setType(courseComment.getType());
                userMessage.setStatus(0);
                userMessage.setCreateTime(new Date());
                userMessage.setUpdateTime(new Date());
                userMessage.setCreateUser(courseComment.getAccount());
                userMessage.setUpdateUser(courseComment.getAccount());
                userMessage.setDel(0);
                userMessagesDao.create(userMessage);
            }
        }
    }

    @Override 
    public void update(CourseComment entity ) {
        courseCommentDao.update(entity);
    }

    @Override 
    public void updateSelectivity(CourseComment entity ) {
        courseCommentDao.updateSelectivity(entity);
    }

    @Override 
    public void delete(CourseComment courseComment ) {
        courseCommentDao.delete(courseComment);
    }

    @Override
    public void deleteLogic(CourseComment entity ) {
        courseCommentDao.deleteLogic(entity);
    }
}
