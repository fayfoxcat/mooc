package org.fox.mooc.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/20-9:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseComment extends BaseEntity {
    private static final long serialVersionUID = 1685305672224227496L;
    /**
     * 撰写评论用户account
     */
    private String account;

    /**
     * 评论对象account
     */
    private String toAccount;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 引用id
     */
    private Long refId;

    /**
     * 引用内容
     */
    private String refContent;

    /**
     * 类型：0-评论；1-答疑QA
     */
    private Integer type;


    /**
     * 用户username
     */
    private String username;

    /**
     * 用户头像
     */
    private String header;


    /**
     * 课程名称
     */
    private String courseName;
}
