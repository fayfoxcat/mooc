package org.fox.mooc.service.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseComment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CourseCommentServiceTest extends BaseTest{
    @Autowired
    private CourseCommentService courseCommentService;

    @Test
    public void byIdTest() {
        CourseComment courseComment = courseCommentService.getById(1L);
        System.out.println(courseComment.toString());
    }

}
