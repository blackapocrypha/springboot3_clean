package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.dto.AclPathRoleDTO;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclPathRoleQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclPathRoleService;
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
* @since 2023-05-30
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-path-role")
public class AclPathRoleController {
    @Resource
    private IAclPathRoleService service;

    /**
    * 分页查询
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclPathRoleQuery query) {
        return service.selectAll(query);
    }


    /**
     * 根据角色查询对应的接口
     * @param id id
     * @return JsonResult
     */
    @PostMapping(value = "/selectPathByRoleId")
    public JsonResult selectPathByRoleId(@RequestParam(value = "id")  Long id){ return service.selectPathByRoleId(id);}

    /**
    * 插入
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入接口角色关系")
    public JsonResult insert(@ModelAttribute AclPathRoleDTO record) {
        return service.add(record);
    }

    /**
    * 根据主键修改
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键修改角色关系")
    @PostMapping(value = "/update")
    public JsonResult update(@ModelAttribute AclPathRoleDTO record) {
        return service.updateByKey(record);
    }


    /**
     * 根据角色更新对应的接口
     * @param record 修改信息
     * @return JsonResult
     */
    @SysLog(client = ClientEnum.ADMIN,act = "根据角色更新对应的接口")
    @PostMapping(value = "/updatePathByRole")
    public JsonResult updatePathByRole(@ModelAttribute AclPathRoleDTO record) {
        return service.updatePathByRole(record);
    }


    /**
    * 根据主键删除
    *
    * @param id 主键id
    * @return JsonResult
    */
    @SysLog(client = ClientEnum.ADMIN,act = "根据主键删除对应的角色接口关系")
    @PostMapping(value = "/delete")
    public JsonResult delete(@RequestParam(value = "id")  Long id) {
        return service.deleteByKey(id);
    }

}
