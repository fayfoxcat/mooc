package org.fox.mooc.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author by fairyfox
 * *2020/2/29-10:28
 */
@Data
public class UserFollowStudyRecord {
    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 章节id
     */
    private Long sectionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户头像
     */
    private String header;

    /**
     * 关注用户id
     */
    private Long followId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 章节名称
     */
    private String sectionName;

    /**
     * 创建时间
     */
    private Date createTime;
}
