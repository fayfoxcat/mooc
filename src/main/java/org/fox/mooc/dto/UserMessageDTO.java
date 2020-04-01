package org.fox.mooc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.UserMessage;

/**
 * @Author by fairyfox
 * *2020/3/16-13:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserMessageDTO extends UserMessage {
    private static final long serialVersionUID = -4580070437473259404L;

    /**
     * 用户头像
     */
    private String header;

    private String refContent;
}
