package org.fox.mooc.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.course.CourseSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/21-19:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseSectionVO extends CourseSection {
    private static final long serialVersionUID = 7301030796797078744L;
    //小节
    private List<CourseSection> sections = new ArrayList<>();
}
