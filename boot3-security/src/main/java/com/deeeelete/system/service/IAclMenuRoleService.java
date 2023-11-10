package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclMenuRole;
import com.deeeelete.system.entity.dto.AclMenuRoleDTO;
import com.deeeelete.system.entity.query.AclMenuRoleQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 *  服务类
 *
 * @author bin.xie
 * @since 2023-06-24
*/
public interface IAclMenuRoleService extends IService<AclMenuRole> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectCurrentUserMenu(Long id);

    /**
     * 根据角色查询菜单
     * @param id  角色id
     * @return JsonResult
     */
     JsonResult selectCurrentRoleMenu(Long id);




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
      JsonResult updateByKey(AclMenuRoleDTO record);

      /**
      * 插入
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclMenuRole record);


 }
