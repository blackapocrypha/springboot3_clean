package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclPathRole;
import com.deeeelete.system.entity.dto.AclPathRoleDTO;
import com.deeeelete.system.entity.query.AclPathRoleQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 *  服务类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public interface IAclPathRoleService extends IService<AclPathRole> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclPathRoleQuery query);

    /**
     * 根据角色id查询接口
     * @param id
     * @return JsonResult
     */
     JsonResult selectPathByRoleId(Long id);


    /**
      * 根据主键删除
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclPathRoleDTO record);

    /**
     * 根据角色修改接口
     * @param record 相关信息
     * @return JsonResult
     */
    JsonResult updatePathByRole(AclPathRoleDTO record);

      /**
      * 插入
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclPathRoleDTO record);


 }
