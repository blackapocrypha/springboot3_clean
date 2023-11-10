package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclRoleQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
* 角色表 前端控制器
*
* @author bin.xie
* @since 2023-05-30
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-role")
public class AclRoleController {
    @Resource
    private IAclRoleService service;

    /**
    * 分页查询角色表
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclRoleQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入角色表
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入角色")
    public JsonResult insert(@ModelAttribute AclRole record) {
        return service.add(record);
    }

    /**
    * 根据主键修改角色表
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键修改角色")
    public JsonResult update(@ModelAttribute AclRole record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除角色表
    *
    * @param id 主键id
    * @return JsonResult
    */
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键删除角色")
    @PostMapping(value = "/delete")
    public JsonResult delete(@RequestParam Long id) {
        return service.deleteByKey(id);
    }

}
