package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclPath;
import com.deeeelete.system.entity.query.AclPathQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 *  服务类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public interface IAclPathService extends IService<AclPath> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclPathQuery query);


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
      JsonResult updateByKey(AclPath record);

      /**
      * 插入
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclPath record);


    /**
     * 初始化权限
     */
    void initPath();
 }
