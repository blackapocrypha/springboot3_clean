package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.query.AclRoleQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 * 角色表 服务类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public interface IAclRoleService extends IService<AclRole> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclRoleQuery query);


    /**
      * 根据主键删除角色表
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改角色表
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclRole record);

      /**
      * 插入角色表
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclRole record);


 }
