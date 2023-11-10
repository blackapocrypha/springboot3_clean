package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("acl_user")
public class AclUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 头像
     */
    @TableField("user_image")
    private String userImage;

    /**
     * 性别 0无 1男 2女
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField("gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date gmtModified;
}
