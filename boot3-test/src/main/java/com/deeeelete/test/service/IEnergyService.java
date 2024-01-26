package com.deeeelete.test.service;

import com.deeeelete.test.entity.Energy;
import com.deeeelete.test.entity.vo.EnergyVO;
import com.deeeelete.test.entity.query.EnergyQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;


/**
 *  服务类
 *
 * @author bin.xie
 * @since 2024-01-26
*/
public interface IEnergyService extends IService<Energy> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(EnergyQuery query);


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
      JsonResult updateByKey(EnergyVO record);

      /**
      * 插入
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(EnergyVO record);


 }
