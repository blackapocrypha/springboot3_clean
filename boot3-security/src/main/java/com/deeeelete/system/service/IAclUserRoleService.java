package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclUserRole;
import com.deeeelete.system.entity.dto.AclUserRoleDTO;
import com.deeeelete.system.entity.query.AclUserRoleQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 * 角色用户绑定 服务类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public interface IAclUserRoleService extends IService<AclUserRole> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclUserRoleQuery query);


    /**
     * 根据主键删除角色用户绑定
     * @param userId 用户id
     * @param roleId 角色id
     * @return JsonResult
     */
      JsonResult deleteByKey(Long userId,Long roleId);

      /**
      * 根据主键修改角色用户绑定
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclUserRoleDTO record);

      /**
      * 插入角色用户绑定
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclUserRoleDTO record);


 }
