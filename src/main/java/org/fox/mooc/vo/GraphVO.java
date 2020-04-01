package org.fox.mooc.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author by fairyfox
 * *2020/3/20-10:56
 */
@Data
public class GraphVO {
    /**
     * graph横坐标时间
     */
    private Date graphTime;

    /**
     * graph纵坐标数据
     */
    private Integer count;
}
