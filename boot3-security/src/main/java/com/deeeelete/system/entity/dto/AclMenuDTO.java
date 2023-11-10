package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclMenu;
import lombok.Data;

import java.util.List;

/**
 * 菜单 DTO类
 *
 * @author bin.xie
 * @since 2023-06-24
*/
@Data
public class AclMenuDTO extends AclMenu {


    /**
     * 子节点
     */
    List<AclMenuDTO> children;
}
