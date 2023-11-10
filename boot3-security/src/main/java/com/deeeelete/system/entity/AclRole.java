package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("acl_role")
public class AclRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 代码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 备注
     */
    @TableField("role_remark")
    private String roleRemark;

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

    /**
     * 逻辑删除 0否 1是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
