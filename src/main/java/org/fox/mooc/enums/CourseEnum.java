package org.fox.mooc.enums;

import lombok.Data;

/**
 * @Author by fairyfox
 * *2020/2/20-11:48
 * 课程收藏
 */
public enum CourseEnum {
    FREE(1, "免费"), //免费
    FREE_NOT(0, "付费"), //收费

    ONSALE(1, "上架"), //上架
    ONSALE_NOT(0, "下架"), //下架

    COLLECTION_CLASSIFY_COURSE(1, "收藏");
    private Integer value;
    private String stateInfo;

    private CourseEnum(int value, String stateInfo) {
        this.value = value;
        this.stateInfo = stateInfo;
    }

    public Integer getValue() {
        return value;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
