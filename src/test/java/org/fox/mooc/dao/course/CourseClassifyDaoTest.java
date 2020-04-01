package org.fox.mooc.dao.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseClassify;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseClassifyDaoTest extends BaseTest{
    @Autowired
    private CourseClassifyDao courseClassifyDao;

    @Test
    public void queryAllTest() {
        List<CourseClassify> courseClassify = courseClassifyDao.queryAll();
        Iterator<CourseClassify> iterator = courseClassify.iterator();
        while (iterator.hasNext()) {
            CourseClassify cc = iterator.next();
            System.out.println(cc.getName());
        }
    }

    @Test
    public void queryByConditionTest() {
        CourseClassify cc = new CourseClassify();
        cc.setCode("computer");
        List<CourseClassify> courseClassifyList = courseClassifyDao.queryByCondition(cc);
        Iterator<CourseClassify> iterator = courseClassifyList.iterator();
        while (iterator.hasNext()) {
            CourseClassify c = iterator.next();
            System.out.println(cc.getName());
        }
    }
}
