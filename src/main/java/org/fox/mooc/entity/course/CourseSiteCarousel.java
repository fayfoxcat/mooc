package org.fox.mooc.entity.course;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/12-17:27
 */
@Data
public class CourseSiteCarousel extends BaseEntity {
    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String picture;

    /**
     * 链接
     */
    private String url;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 是否可用
     */
    private Integer enable;
}
