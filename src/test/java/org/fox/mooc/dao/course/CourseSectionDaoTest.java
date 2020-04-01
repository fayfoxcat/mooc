package org.fox.mooc.dao.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.entity.course.Course;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseSectionDaoTest extends BaseTest{
    @Autowired
    private CourseSectionDao courseSectionDao;

    @Test
    public void byIdCountTest() {
        System.out.println(courseSectionDao.byIdCount(19960101L));
    }

}
