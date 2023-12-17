package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.AclPath;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclPathQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclPathService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
*  接口管理
*
* @author bin.xie
* @since 2023-05-30
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-path")
public class AclPathController {
    @Resource
    private IAclPathService service;

    /**
    * 分页查询
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclPathQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "新增接口")
    public JsonResult insert(@ModelAttribute AclPath record) {
        return service.add(record);
    }

    /**
    * 根据主键修改
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "更新接口")
    public JsonResult update(@ModelAttribute AclPath record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除
    *
    * @param id 主键id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    @SysLog(client = ClientEnum.ADMIN,act = "删除接口")
    public JsonResult delete(@RequestParam(value = "id")  Long id) {
        return service.deleteByKey(id);
    }

}
