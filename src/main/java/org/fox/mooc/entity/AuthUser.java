package org.fox.mooc.entity;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.common.BaseEntity;

/**
 * 系统用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthUser extends BaseEntity {

    private static final long serialVersionUID = 8643666439560670258L;
    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 头像
     */
    private String header;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 状态：待审核（0），审核通过（1），默认（2），审核未通过（3），禁用（5）
     */
    private Integer status;

    /**
     * 角色：超级管理员（0），普通用户（1），已提交声请（2），讲师（3），
     */
    private Integer role;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 学历：大专、本科、硕士、博士、博士后
     */
    private String education;

    /**
     * 资格证书
     */
    private String certificate;

    /**
     * 大学全称
     */
    private String collegeName;

    /**
     * 头衔
     */
    private String title;

    /**
     * 签名
     */
    private String sign;

    /**
     * 最后一次登录时间
     */
    private Date loginTime;

    /**
     * 最后一次登录IP
     */
    private String ip;

    /**
     * 所在省份
     */
    private String province;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 所在地区
     */
    private String district;

    /**
     * 推荐权重
     */
    private Integer weight;

}

