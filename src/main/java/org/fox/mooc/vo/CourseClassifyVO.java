package org.fox.mooc.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.entity.course.CourseClassify;

/**
 * @Author by fairyfox
 * *2020/2/11-16:51
 * 页面展示 value object
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseClassifyVO extends CourseClassify {

    private static final long serialVersionUID = 5061808200313987851L;
    //子分类列表
    private List<CourseClassify> subClassifyList;

    //课程推荐列表
    private List<Course> recomdCourseList;
}
