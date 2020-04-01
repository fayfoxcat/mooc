package org.fox.mooc.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/29-10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCourseSection extends BaseEntity {
    private static final long serialVersionUID = 3234280138420963862L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 章节id
     */
    private Long sectionId;

    /**
     * 章节名
     */
    private String sectionName;

    /**
     * 状态：0-学习中；1-学习结束
     */
    private Integer status;

    /**
     * 进度
     */
    private Integer rate;
}
