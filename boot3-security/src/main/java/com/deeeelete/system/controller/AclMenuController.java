package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.system.entity.AclMenu;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclMenuQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclMenuService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
*
* 菜单 前端控制器
*
* @author bin.xie
* @since 2023-06-24
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-menu")
public class AclMenuController {
    @Resource
    private IAclMenuService service;

    /**
    * 分页查询菜单
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclMenuQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入菜单
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入菜单")
    public JsonResult insert(@ModelAttribute AclMenu record) {
        return service.add(record);
    }

    /**
    * 根据主键修改菜单
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "更新菜单")
    public JsonResult update(@ModelAttribute AclMenu record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除菜单
    *
    * @param id 主键id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    @SysLog(client = ClientEnum.ADMIN,act = "删除菜单")
    public JsonResult delete(@RequestParam Long id) {
        return service.deleteByKey(id);
    }


    /**
     * 表单提交 Date类型数据绑定
     *
     * @param binder 网页数据绑定
     * @see [类、类#方法、类#成员]
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
