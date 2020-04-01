package org.fox.mooc.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.UserMessage;

/**
 * @Author by fairyfox
 * *2020/3/31-23:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserMessageVO extends UserMessage {

    /**
     * 消息发送者头像
     */
    private String header;
}
