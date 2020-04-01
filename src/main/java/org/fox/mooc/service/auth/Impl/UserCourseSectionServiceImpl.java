package org.fox.mooc.service.auth.Impl;
import org.fox.mooc.dao.auth.AuthUserDao;
import org.fox.mooc.dao.auth.UserCourseSectionDao;
import org.fox.mooc.dao.course.CourseSectionDao;
import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserCourseSection;
import org.fox.mooc.service.auth.UserCourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/29-12:17
 */
@Service
public class UserCourseSectionServiceImpl implements UserCourseSectionService {

    @Autowired
    private UserCourseSectionDao userCourseSectionDao;

    @Autowired
    private CourseSectionDao courseSectionDao;

    @Autowired
    private AuthUserDao authUserDao;

    @Override
    public UserCourseSection getById(Long id) {
        return null;
    }

    @Override
    public List<UserCourseSection> queryAll(UserCourseSection queryEntity) {
        return null;
    }

    /**
     * 返回最新近学习记录
     * @param userCourseSection
     * @return
     */
    @Override
    public UserCourseSection queryLatest(UserCourseSection userCourseSection) {
        return userCourseSectionDao.queryLatest(userCourseSection);
    }

    /*用户加入学习课程数*/
    @Override
    public Integer getStudyCourseCount(UserCourseSection userCourseSection) {
        return userCourseSectionDao.getStudyCourseCount(userCourseSection);
    }

    @Override
    public List<UserCourseSectionDTO> queryPage(UserCourseSectionDTO userCourseSectionDTO, Integer rowIndex, Integer pageSize) {
        return userCourseSectionDao.queryPage(userCourseSectionDTO,rowIndex,pageSize);
    }

    @Override
    public List<UserCourseSectionDTO> getStudyCourse(Long userId, Integer rowIndex, Integer pageSize) {
        return userCourseSectionDao.getStudyCourse(userId,rowIndex,pageSize);
    }

    @Override
    public void createSelectivity(UserCourseSection userCourseSection) {
        //根据传入sectionId查询sectionName
        String sectionName = courseSectionDao.getById(userCourseSection.getSectionId()).getName();
        userCourseSection.setSectionName(sectionName);
        //查询已学习章节记录数
        Integer recordCount = userCourseSectionDao.getRecordCount(userCourseSection);
        //查询当前课程总章节数
        Integer CourseCount = courseSectionDao.byIdCount(userCourseSection.getCourseId());
        //计算学习进度比率
        int rate = recordCount*100/CourseCount;
        userCourseSection.setRate(rate);
        AuthUser au = authUserDao.getById(userCourseSection.getUserId());
        userCourseSection.setUpdateUser(au.getUsername());
        userCourseSection.setUpdateTime(new Date());

        //判断记录数是否存在，决定更新、创建
        UserCourseSection byRecord = userCourseSectionDao.getByRecord(userCourseSection);
        if (byRecord == null){
            userCourseSection.setCreateUser(au.getUsername());
            userCourseSection.setCreateTime(new Date());
            userCourseSectionDao.createSelectivity(userCourseSection);
        }else{//更新
            userCourseSection.setId(byRecord.getId());
            userCourseSectionDao.updateSelectivity(userCourseSection);
        }

    }

    @Override
    public void update(UserCourseSection entity) {

    }

    @Override
    public void updateSelectivity(UserCourseSection entity) {

    }

    @Override
    public void remove(UserCourseSection userCourseSection) {
        userCourseSectionDao.remove(userCourseSection);
    }

    @Override
    public void delete(UserCourseSection entity) {

    }

    @Override
    public void deleteLogic(UserCourseSection entity) {

    }
}
