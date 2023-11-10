package com.deeeelete.system.entity.convert;

import com.deeeelete.system.entity.AclUserRole;
import com.deeeelete.system.entity.dto.AclUserRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AclUserRoleMapping {

    AclUserRoleMapping INSTANCE = Mappers.getMapper(AclUserRoleMapping.class);

    AclUserRole toPO(AclUserRoleDTO aclUserRoleDTO);

}
