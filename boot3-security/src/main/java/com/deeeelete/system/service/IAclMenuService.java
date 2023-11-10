package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclMenu;
import com.deeeelete.system.entity.query.AclMenuQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 * 菜单 服务类
 *
 * @author bin.xie
 * @since 2023-06-24
*/
public interface IAclMenuService extends IService<AclMenu> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclMenuQuery query);


    /**
      * 根据主键删除菜单
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改菜单
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclMenu record);

      /**
      * 插入菜单
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclMenu record);


 }
