package org.fox.mooc.service.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseClassify;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseClassifyServiceTest extends BaseTest{
    @Autowired
    private CourseClassifyService courseClassifyService;

    @Test
    public void queryAllTest() {
        List<CourseClassify> courseClassify = courseClassifyService.queryAll();
        Iterator<CourseClassify> iterator = courseClassify.iterator();
        while (iterator.hasNext()) {
            CourseClassify cc = iterator.next();
            System.out.println(cc.getName());
        }
    }

    @Test
    public void queryByConditionTest() {
        CourseClassify courseClassify = courseClassifyService.getByCode("0");
        System.out.println(courseClassify.getName());

    }

}
