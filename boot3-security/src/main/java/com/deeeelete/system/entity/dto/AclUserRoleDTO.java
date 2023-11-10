package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.AclUserRole;
import lombok.Data;

import java.util.List;

/**
 * 角色用户绑定 DTO类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
@Data
public class AclUserRoleDTO extends AclUserRole {

    /**
     * 多角色，逗号隔开
     */
    private String allRoles;

}
