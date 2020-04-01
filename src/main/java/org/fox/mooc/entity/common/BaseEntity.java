package org.fox.mooc.entity.common;

import lombok.Data;
import org.fox.mooc.entity.common.LongModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author by fairyfox
 * *2020/2/11-11:30
 */
@Data
public class BaseEntity extends LongModel{
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人(username)
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后一位更新人(username)
     */
    private String updateUser;

    /**
     * 逻辑删除
     */
    private Integer del;
}
