package org.fox.mooc.entity.course;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

import java.math.BigDecimal;
/**
 * @Author by fairyfox
 * *2020/2/11-16:51
 */
@Data
public class Course extends BaseEntity {
    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程分类
     */
    private String classify;

    /**
     * 课程分类名称
     */
    private String classifyName;

    /**
     * 课程二级分类
     */
    private String subClassify;

    /**
     * 课程二级分类名称
     */
    private String subClassifyName;

    /**
     * 归属account
     */
    private String account;

    /**
     * 归属人
     */
    private String ownerName;

    /**
     * 是否免费：0-否，1-是
     */
    private Integer free;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 时长
     */
    private String courseTime;

    /**
     * 未上架（0）、上架（1）
     */
    private Integer onsale;

    /**
     * 课程描述
     */
    private String brief;

    /**
     * 课程图片
     */
    private String picture;

    /**
     * 未推荐（0）、推荐（1）
     */
    private Integer recommend;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 学习人数
     */
    private Integer studyCount;
}
