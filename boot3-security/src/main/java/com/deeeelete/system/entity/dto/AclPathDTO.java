package com.deeeelete.system.entity.dto;

import com.deeeelete.system.entity.AclPath;
import lombok.Data;

import java.util.List;

/**
 *  DTO类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
@Data
public class AclPathDTO extends AclPath {

    /**
     * 下级
     */
    List<AclPathDTO> children;

}
