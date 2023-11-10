package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclPathRole;
import lombok.Data;
/**
 *  DTO类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
@Data
public class AclPathRoleDTO extends AclPathRole {

    /**
     * 角色id
     */
    private String roleIds;

    /**
     * 路径id
     */
    private String pathIds;
}
