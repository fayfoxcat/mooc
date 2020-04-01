package org.fox.mooc.entity;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/29-10:25
 */
@Data
public class UserFollows extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注的用户id
     */
    private Long followId;
}
