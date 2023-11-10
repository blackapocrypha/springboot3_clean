package com.deeeelete.system.service;

import com.deeeelete.system.entity.SysHistoryLogs;
import com.deeeelete.system.entity.query.SysHistoryLogsQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 * 系统日志 服务类
 *
 * @author bin.xie
 * @since 2023-10-24
*/
public interface ISysHistoryLogsService extends IService<SysHistoryLogs> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(SysHistoryLogsQuery query);


    /**
      * 根据主键删除系统日志
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改系统日志
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(SysHistoryLogs record);

      /**
      * 插入系统日志
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(SysHistoryLogs record);


 }
