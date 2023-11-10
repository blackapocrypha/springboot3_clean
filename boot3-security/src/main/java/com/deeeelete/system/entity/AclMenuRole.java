package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author bin.xie
 * @since 2023-06-24
 */
@Getter
@Setter
@TableName("acl_menu_role")
@Accessors(chain = true)
public class AclMenuRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acmr_id", type = IdType.AUTO)
    private Long acmrId;

    /**
     * 菜单id
     */
    @TableField("acmr_menu_id")
    private Long acmrMenuId;

    /**
     * 角色id
     */
    @TableField("acmr_role_id")
    private Long acmrRoleId;

    /**
     * 角色名
     */
    @TableField("acmr_role_name")
    private String acmrRoleName;

    /**
     * 角色代码
     */
    @TableField("acmr_role_code")
    private String acmrRoleCode;
}
