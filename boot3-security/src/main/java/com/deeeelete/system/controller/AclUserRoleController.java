package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.dto.AclUserRoleDTO;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclUserRoleQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclUserRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
* 角色用户绑定 前端控制器
*
* @author bin.xie
* @since 2023-05-30
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-user-role")
public class AclUserRoleController {
    @Resource
    private IAclUserRoleService service;

    /**
    * 分页查询角色用户绑定
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclUserRoleQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入角色用户绑定
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入角色用户绑定")
    public JsonResult insert(@ModelAttribute AclUserRoleDTO record) {
        return service.add(record);
    }

    /**
    * 根据主键修改角色用户绑定
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键修改角色用户绑定")
    public JsonResult update(@ModelAttribute AclUserRoleDTO record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除角色用户绑定
    *
    * @param userId 主键id
    * @param roleId 角色id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键删除角色用户绑定")
    public JsonResult delete(@RequestParam("userId") Long userId,
                             @RequestParam("roleId") Long roleId) {
        return service.deleteByKey(userId,roleId);
    }

}
