package org.fox.mooc.entity.course;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/11-11:37
 */
@Data
public class CourseClassify extends BaseEntity {
    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 父级别id
     */
    private String parentCode;

    /**
     * 排序
     */
    private Long sort;
}
