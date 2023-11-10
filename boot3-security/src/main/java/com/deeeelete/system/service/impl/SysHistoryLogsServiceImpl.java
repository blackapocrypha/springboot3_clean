package com.deeeelete.system.service.impl;

import com.deeeelete.system.entity.SysHistoryLogs;
import com.deeeelete.system.entity.query.SysHistoryLogsQuery;
import com.deeeelete.system.entity.dto.SysHistoryLogsDTO;
import com.deeeelete.system.mapper.SysHistoryLogsMapper;
import com.deeeelete.system.service.ISysHistoryLogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * 系统日志 服务实现类
 *
 * @author bin.xie
 * @since 2023-10-24
 */
@Service
public class SysHistoryLogsServiceImpl extends ServiceImpl<SysHistoryLogsMapper, SysHistoryLogs> implements ISysHistoryLogsService {

    /**
    * 分页查询系统日志
    *
    * @return JsonResult
    */
    @Override
    public JsonResult selectAll(SysHistoryLogsQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<SysHistoryLogs> page = query.buildPage(this);
        List<SysHistoryLogsDTO> records = EntityUtil.parentListToChildList(page.getRecords(), SysHistoryLogsDTO.class);
        if(ListUtil.isNotEmpty(records)){
            jsonResult.buildTrue();
            jsonResult.setData(records);
            jsonResult.setTotalsize(page.getTotal());
        }else {
            jsonResult.buildFalse("无数据");
        }
        return jsonResult;
    }

    /**
     * 根据主键删除系统日志
     *
     * @param id 主键
     * @return JsonResult
    */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        if(removeById(id)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改系统日志
     *
     * @param record 修改信息
     * @return JsonResult
    */
    @Override
    public JsonResult updateByKey(SysHistoryLogs record) {
        JsonResult jsonResult = new JsonResult();
        if(updateById(record)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入系统日志
     *
     * @param record 插入信息
     * @return JsonResult
    */
    @Override
    public JsonResult add(SysHistoryLogs record) {
        JsonResult jsonResult = new JsonResult();
        if(save(record)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
