package com.deeeelete.system.controller;

import com.deeeelete.system.entity.AclWhiteList;
import com.deeeelete.system.entity.query.AclWhiteListQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclWhiteListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
* 白名单 前端控制器
*
* @author
* @since 2024-01-25
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-white-list")
public class AclWhiteListController {
    @Resource
    private IAclWhiteListService service;

    /**
    * 分页查询白名单
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclWhiteListQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入白名单
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    public JsonResult insert(@ModelAttribute AclWhiteList record) {
        return service.add(record);
    }

    /**
    * 根据主键修改白名单
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    public JsonResult update(@ModelAttribute AclWhiteList record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除白名单
    *
    * @param id 主键id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    public JsonResult delete(@RequestParam(value = "id") Long id) {
        return service.deleteByKey(id);
    }

}
