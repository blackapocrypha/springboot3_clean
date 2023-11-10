package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色用户绑定
 * </p>
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("acl_user_role")
public class AclUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acur_id", type = IdType.AUTO)
    private Long acurId;

    /**
     * 用户Id
     */
    @TableField("acur_user_id")
    private Long acurUserId;

    /**
     * 角色id
     */
    @TableField("acur_role_id")
    private Long acurRoleId;

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
