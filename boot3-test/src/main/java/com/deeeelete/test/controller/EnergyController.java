package com.deeeelete.test.controller;

import com.deeeelete.test.entity.vo.EnergyVO;
import com.deeeelete.test.entity.query.EnergyQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.test.service.IEnergyService;
import org.springframework.web.bind.annotation.*;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
*  前端控制器
*
* @author bin.xie
* @since 2024-01-26
*/
@RestController
@Slf4j
@RequestMapping("/test/energy")
public class EnergyController {
    @Resource
    private IEnergyService service;

    /**
    * 分页查询
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@RequestBody EnergyQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    public JsonResult insert(@RequestBody EnergyVO record) {
        return service.add(record);
    }

    /**
    * 根据主键修改
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PutMapping(value = "/update")
    public JsonResult update(@RequestBody EnergyVO record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除
    *
    * @param id 主键id
    * @return JsonResult
    */
    @DeleteMapping(value = "/delete")
    public JsonResult delete(@RequestParam(value = "id") Long id) {
        return service.deleteByKey(id);
    }

}
