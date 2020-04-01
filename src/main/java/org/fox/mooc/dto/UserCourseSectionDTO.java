package org.fox.mooc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.UserCourseSection;

/**
 * @Author by fairyfox
 * *2020/2/29-10:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCourseSectionDTO extends UserCourseSection {
    private static final long serialVersionUID = -2673184437094265034L;
    /**
     * 授课讲师名
     */
    private String username;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 章节名
     */
    private String sectionName;

    /**
     * 课程描述
     */
    private String brief;

   /**
     * 课程图片
     */
    private String picture;
}
