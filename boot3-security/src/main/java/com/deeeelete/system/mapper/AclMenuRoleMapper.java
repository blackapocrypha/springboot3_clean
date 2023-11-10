package com.deeeelete.system.mapper;

import com.deeeelete.system.entity.AclMenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bin.xie
 * @since 2023-06-24
 */
@Mapper
public interface AclMenuRoleMapper extends BaseMapper<AclMenuRole> {

    /**
     * 根据id查询用户当前拥有的全部菜单Code
     * @param id
     * @return
     */
    List<String> selectAllUserMenuCodeByUserId(@Param("id") Long id,@Param("type")Integer type);
}
