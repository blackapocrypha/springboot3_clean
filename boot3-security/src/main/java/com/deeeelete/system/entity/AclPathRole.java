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
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("acl_path_role")
@Accessors(chain = true)
public class AclPathRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acpr_id", type = IdType.AUTO)
    private Long acprId;

    /**
     * 路径id
     */
    @TableField("acpr_path_id")
    private Long acprPathId;

    /**
     * 角色id
     */
    @TableField("acpr_role_id")
    private Long acprRoleId;

    /**
     * 角色名
     */
    @TableField("acpr_role_name")
    private String acprRoleName;

    /**
     * 角色代码
     */
    @TableField("acpr_role_code")
    private String acprRoleCode;
}
