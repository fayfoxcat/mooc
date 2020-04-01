package org.fox.mooc.entity.course;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/20-9:51
 */
@Data
public class CourseSection extends BaseEntity {
    /**
     * 归属课程id
     */
    private Long courseId;

    /**
     * 父章节id，（只有2级）
     */
    private Long parentId;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 时长
     */
    private String time;

    /**
     * 未上架（0）、上架（1）
     */
    private Integer onsale;

    /**
     * 视频url
     */
    private String videoUrl;
}
