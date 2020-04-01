package org.fox.mooc.service.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.entity.course.CourseSection;
import org.fox.mooc.enums.CourseEnum;
import org.fox.mooc.vo.CourseSectionVO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CourseServiceTest extends BaseTest{
    @Autowired
    private CourseSectionService courseSectionService;

    @Autowired
    private CourseService courseService;

    @Test
    @Ignore
    public void queryAllTest() {
        List<CourseSectionVO> resultList = new ArrayList<>();
        CourseSection courseSection = new CourseSection();
        courseSection.setCourseId(19960101L);
        courseSection.setOnsale(CourseEnum.ONSALE.getValue());//上架
        Iterator<CourseSection> iterator = courseSectionService.queryAll(courseSection).iterator();
        while (iterator.hasNext()) {
            CourseSection cc = iterator.next();
            System.out.println(cc);
        }
    }

    @Test
    public void courseListTest() {
        CourseQueryDTO courseQueryDTO = new CourseQueryDTO();
        courseQueryDTO.setOnsale(null);
        List<Course> courseList = courseService.queryList(courseQueryDTO);
        System.out.println(courseList.toString());
    }
}
