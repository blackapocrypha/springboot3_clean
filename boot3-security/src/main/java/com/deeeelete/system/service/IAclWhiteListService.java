package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclWhiteList;
import com.deeeelete.system.entity.query.AclWhiteListQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 * 白名单 服务类
 *
 * @author
 * @since 2024-01-25
*/
public interface IAclWhiteListService extends IService<AclWhiteList> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclWhiteListQuery query);


    /**
      * 根据主键删除白名单
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改白名单
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclWhiteList record);

      /**
      * 插入白名单
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclWhiteList record);


 }
