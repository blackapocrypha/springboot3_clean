package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.AclMenuRole;
import com.deeeelete.system.entity.dto.AclMenuRoleDTO;
import com.deeeelete.system.entity.enums.ClientEnum;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclMenuRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
*  前端控制器
*
* @author bin.xie
* @since 2023-06-24
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-menu-role")
public class AclMenuRoleController {
    @Resource
    private IAclMenuRoleService service;

    /**
    * 查询当前用户的所属菜单
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectCurrentUserMenu")
    public JsonResult selectCurrentUserMenu(Long id) {
        return service.selectCurrentUserMenu(id);
    }


    /**
     * 查询当前角色所属菜单
     * @param id 角色
     * @return JsonResult
     */
    @PostMapping(value = "/selectCurrentRoleMenu")
    public JsonResult selectCurrentRoleMenu(@RequestParam(value = "id") Long id) {
        return service.selectCurrentRoleMenu(id);
    }



    /**
    * 插入
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入菜单角色对应关系")
    public JsonResult insert(@ModelAttribute AclMenuRole record) {
        return service.add(record);
    }

    /**
    * 根据主键修改
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "修改菜单角色对应关系")
    public JsonResult update(@ModelAttribute AclMenuRoleDTO record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除
    *
    * @param id 主键id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    @SysLog(client = ClientEnum.ADMIN,act = "删除菜单角色对应关系")
    public JsonResult delete(@RequestParam(value = "id")  Long id) {
        return service.deleteByKey(id);
    }

}
