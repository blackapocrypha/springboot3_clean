package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.AclUser;
import lombok.Data;

import java.util.List;

/**
 * 用户 DTO类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
@Data
public class AclUserDTO extends AclUser {

    /**
     * 验证码
     */
    private String code;

    /**
     * 校验用的uuid
     */
    private String uuid;

    /**
     * 当前用户所拥有的角色
     */
    private List<AclRole> myRoles;
}
