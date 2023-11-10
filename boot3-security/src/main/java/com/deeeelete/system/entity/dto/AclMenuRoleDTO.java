package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclMenuRole;
import lombok.Data;
/**
 *  DTO类
 *
 * @author bin.xie
 * @since 2023-06-24
*/
@Data
public class AclMenuRoleDTO extends AclMenuRole {

    /**
     * 菜单id 多个用逗号隔开
     */
    private String menuIds;

}
