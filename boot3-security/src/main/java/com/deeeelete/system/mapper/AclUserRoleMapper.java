package com.deeeelete.system.mapper;

import com.deeeelete.system.entity.AclUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色用户绑定 Mapper 接口
 * </p>
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Mapper
public interface AclUserRoleMapper extends BaseMapper<AclUserRole> {

}
