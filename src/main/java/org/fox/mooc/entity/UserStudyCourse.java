package org.fox.mooc.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/3/24-23:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserStudyCourse  extends BaseEntity {
    private static final long serialVersionUID = 3330441683856525419L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 课程id
     */
    private Long courseId;
}
