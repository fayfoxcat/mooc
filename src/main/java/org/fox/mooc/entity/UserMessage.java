package org.fox.mooc.entity;

import lombok.Data;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * @Author by fairyfox
 * *2020/2/29-10:44
 */
@Data
public class UserMessage extends BaseEntity {
    /**
     * 消息接收用户id
     */
    private Long userId;

    /**
     * 消息发起用户id
     */
    private Long sendUserId;

    /**
     * 消息发起用户名称
     */
    private String sendUserName;

    /**
     * 引用id
     */
    private String refId;

    /**
     * 引用内容
     */
    private String content;

    /**
     * 通知的类型，1-评论，2-关注，3-答疑
     */
    private Integer type;

    /**
     * 未读（0）、已读（1）
     */
    private Integer status;
}
