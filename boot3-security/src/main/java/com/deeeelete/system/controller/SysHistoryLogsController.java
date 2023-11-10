package com.deeeelete.system.controller;

import com.deeeelete.system.entity.query.SysHistoryLogsQuery;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.ISysHistoryLogsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
*
* 系统日志 前端控制器
*
* @author bin.xie
* @since 2023-10-24
*/
@RestController
@Slf4j
@RequestMapping("/system/sys-history-logs")
public class SysHistoryLogsController {
    @Resource
    private ISysHistoryLogsService service;

    /**
    * 分页查询系统日志
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute SysHistoryLogsQuery query) {
        return service.selectAll(query);
    }


}
