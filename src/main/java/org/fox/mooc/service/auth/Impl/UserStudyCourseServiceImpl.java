package org.fox.mooc.service.auth.Impl;

import org.fox.mooc.dao.auth.UserCourseSectionDao;
import org.fox.mooc.dao.auth.UserStudyCourseDao;
import org.fox.mooc.entity.UserCourseSection;
import org.fox.mooc.entity.UserStudyCourse;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.auth.UserStudyCourseService;
import org.fox.mooc.vo.UserStudyCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/3/24-23:52
 */
@Service
public class UserStudyCourseServiceImpl implements UserStudyCourseService {

    @Autowired
    private UserStudyCourseDao userStudyCourseDao;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserCourseSectionDao userCourseSectionDao;

    @Override
    public UserStudyCourse getById(Long id) {
        return userStudyCourseDao.getById(id);
    }

    @Override
    public List<UserStudyCourseVO> queryPage(UserStudyCourse userstudyCourse, Integer rowIndex, Integer pageSize) {
        return userStudyCourseDao.queryPage(userstudyCourse,rowIndex,pageSize);
    }

    @Override
    public Integer getTotalItemsCount(UserStudyCourse studyCourse) {
        return userStudyCourseDao.getTotalItemsCount(studyCourse);
    }

    @Override
    @Transactional
    public void createSelectivity(UserStudyCourse studyCourse) {
        studyCourse.setUpdateTime(new Date());
        studyCourse.setCreateTime(new Date());
        userStudyCourseDao.createSelectivity(studyCourse);
        //用户加入课程，同时该课程学习人数新增
        Course course = courseService.getById(studyCourse.getCourseId());
        //新增1
        course.setStudyCount(course.getStudyCount()+1);
        courseService.updateStatus(course);
    }

    @Override
    public void updateSelectivity(UserStudyCourse studyCourse) {
        userStudyCourseDao.updateSelectivity(studyCourse);
    }

    @Override
    @Transactional
    public void delete(UserStudyCourse studyCourse) {
        userStudyCourseDao.delete(studyCourse);
        //用户移除课程、同时清空用户学习进度、记录
        UserCourseSection userCourseSection = new UserCourseSection();
        userCourseSection.setUserId(studyCourse.getUserId());
        userCourseSection.setCourseId(studyCourse.getCourseId());
        userCourseSectionDao.delete(userCourseSection);
    }

    @Override
    public void deleteLogic(UserStudyCourse studyCourse) {
        userStudyCourseDao.deleteLogic(studyCourse);
    }
}
