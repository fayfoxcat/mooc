package org.fox.mooc.dao.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.UserCourseSection;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserCourseSectionDaoTest extends BaseTest{
    @Autowired
    private UserCourseSectionDao userCourseSectionDao;

    @Test
    @Ignore
    public void getRecordCount() {
        UserCourseSection userCourseSection = new UserCourseSection();
        userCourseSection.setCourseId(19960101L);
        userCourseSection.setUserId(202003L);
        System.out.println(userCourseSectionDao.getRecordCount(userCourseSection));
    }
    @Test
    @Ignore
    public void isRecord() {
        UserCourseSection userCourseSection = new UserCourseSection();
        userCourseSection.setCourseId(19960101L);
        userCourseSection.setUserId(202003L);
        userCourseSection.setSectionId(5L);
        UserCourseSection byRecord = userCourseSectionDao.getByRecord(userCourseSection);
        System.out.println(byRecord.getId());
    }

    @Test
    @Ignore
    public void recordlist() {
        Long userId = 202003L;
        UserCourseSectionDTO userCourseSectionDTO = new UserCourseSectionDTO();
        userCourseSectionDTO.setUserId(userId);
        int rowIndex = 0;
        int pageSize = 10;
        List<UserCourseSectionDTO> userCourseSectionDTOS = userCourseSectionDao.queryPage(userCourseSectionDTO, rowIndex, pageSize);
        System.out.println(userCourseSectionDTOS.toString());
        System.out.println(userCourseSectionDTOS.get(0).getCourseId());
    }

    @Test
    public void getStudyCourseDaoTest() {
        int rowIndex = 0;
        int pageSize = 10;
        List<UserCourseSectionDTO> userCourseSectionDTOS = userCourseSectionDao.getStudyCourse(202003L, rowIndex, pageSize);
        System.out.println(userCourseSectionDTOS.toString());
    }

}
