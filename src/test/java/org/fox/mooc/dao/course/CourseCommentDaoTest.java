package org.fox.mooc.dao.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseComment;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/23-14:01
 */
public class CourseCommentDaoTest extends BaseTest{
    @Autowired
    private CourseCommentDao courseCommentDao;

    @Test
    @Ignore
    public void queryListServiceTest() {
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(19960101L);
        List<CourseComment> courseCommentList = courseCommentDao.queryPage(courseComment, 0, 10);
        Iterator<CourseComment> iterator = courseCommentList.iterator();
        while (iterator.hasNext()) {
            CourseComment cc = iterator.next();
            System.out.println(cc.getContent());
        }
    }
}
