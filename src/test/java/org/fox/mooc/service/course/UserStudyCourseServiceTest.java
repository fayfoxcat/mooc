package org.fox.mooc.service.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.UserStudyCourse;
import org.fox.mooc.service.auth.UserStudyCourseService;
import org.fox.mooc.vo.UserStudyCourseVO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


public class UserStudyCourseServiceTest extends BaseTest{
    @Autowired
    private UserStudyCourseService userStudyCourseService;

    @Test
    @Ignore
    public void queryAllTest() {
        UserStudyCourse userStudyCourse = new UserStudyCourse();
        userStudyCourse.setUserId(202011L);
        List<UserStudyCourseVO> list = userStudyCourseService.queryPage(userStudyCourse,0,10);
        System.out.println(list.toString());
    }

    @Test
    @Ignore
    public void countTest() {
        UserStudyCourse userStudyCourse = new UserStudyCourse();
//        userStudyCourse.setUserId(2020L);
        userStudyCourse.setCourseId(19960101L);
        int count = userStudyCourseService.getTotalItemsCount(userStudyCourse);
        System.out.println(count);
    }

    @Test
    public void createTest() {
        UserStudyCourse userStudyCourse = new UserStudyCourse();
        userStudyCourse.setUserId(202011L);
        userStudyCourse.setCourseId(19960105L);
        userStudyCourse.setCreateTime(new Date());
        userStudyCourse.setCreateUser("小甜甜");
        userStudyCourse.setUpdateTime(new Date());
        userStudyCourse.setUpdateUser("小甜甜");
        userStudyCourseService.createSelectivity(userStudyCourse);
    }

}
