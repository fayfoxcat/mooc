package org.fox.mooc.dao.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.vo.GraphVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseDaoTest extends BaseTest{
    @Autowired
    private CourseDao courseDao;

    @Test
    public void queryListServiceTest() {

        CourseQueryDTO courseQueryDTO = new CourseQueryDTO();
        courseQueryDTO.setRowIndex(0);
        courseQueryDTO.setPageSize(5);
        //以学习人数降序排序
        courseQueryDTO.setSortField("weight");
        courseQueryDTO.setCourseName("2.0");
        System.out.println(courseDao.getTotalItemsCount(courseQueryDTO));
        List<Course> courseList = courseDao.queryList(courseQueryDTO);
        Iterator<Course> iterator = courseList.iterator();
        while (iterator.hasNext()) {
            Course cc = iterator.next();
            System.out.println(cc.getCourseName());
        }
    }

    @Test
    public void querygraph() {
        Long courseId = 19960101L;
        List<GraphVO> teacherGraph = courseDao.queryGraph(courseId);
        System.out.println(teacherGraph.toString());

    }

}
