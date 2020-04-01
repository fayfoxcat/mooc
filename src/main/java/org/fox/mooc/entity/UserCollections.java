package org.fox.mooc.entity;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/29-10:24
 */
@Data
public class UserCollections extends BaseEntity {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏课程名
     */
    private String name;
    /**
     * 收藏图片
     */
    private String picture;
    /**
     * 课程简介
     */
    private String brief;

    /**
     * 用户收藏分类
     */
    private Integer classify;

    /**
     * 用户收藏id
     */
    private Long objectId;

    /**
     * 用户收藏备注
     */
    private String tips;


}
