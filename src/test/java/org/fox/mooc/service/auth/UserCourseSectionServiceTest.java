package org.fox.mooc.service.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.UserCourseSection;
import org.fox.mooc.util.PageCalculator;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserCourseSectionServiceTest extends BaseTest{
    @Autowired
    private UserCourseSectionService userCourseSectionService;

    @Test
    @Ignore
    public void createUserCourseSectionTest() {
        UserCourseSection userCourseSection = new UserCourseSection();
        userCourseSection.setUserId(202003L);
        userCourseSection.setCourseId(19960101L);
        userCourseSection.setSectionId(6L);
        userCourseSectionService.createSelectivity(userCourseSection);
    }

    @Test
    public void recordlist() {
        Long userId = 202003L;
        UserCourseSectionDTO userCourseSectionDTO = new UserCourseSectionDTO();
        userCourseSectionDTO.setUserId(userId);
        userCourseSectionDTO.setCourseId(19960102L);
        int pageIndex = 1;
        int pageSize = 10;
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<UserCourseSectionDTO> userCourseSectionDTOS = userCourseSectionService.queryPage(userCourseSectionDTO, rowIndex, pageSize);
        System.out.println(userCourseSectionDTOS.toString());
    }

}
