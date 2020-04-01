package org.fox.mooc.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.course.Course;

/**
 * @Author by fairyfox
 * *2020/3/25-8:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserStudyCourseVO extends Course {
    private static final long serialVersionUID = 5378348361438881093L;

    private Long userId;

    private Long courseId;

}
