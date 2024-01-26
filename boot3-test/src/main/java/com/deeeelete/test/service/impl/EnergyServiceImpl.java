package com.deeeelete.test.service.impl;

import com.deeeelete.test.entity.Energy;
import com.deeeelete.test.entity.query.EnergyQuery;
import com.deeeelete.test.entity.dto.EnergyDTO;
import com.deeeelete.test.entity.vo.EnergyVO;
import com.deeeelete.test.mapper.EnergyMapper;
import com.deeeelete.test.service.IEnergyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import java.util.List;

/**
 *  服务实现类
 *
 * @author bin.xie
 * @since 2024-01-26
 */
@Service
public class EnergyServiceImpl extends ServiceImpl<EnergyMapper, Energy> implements IEnergyService {

    /**
    * 分页查询
    *
    * @return JsonResult
    */
    @Override
    public JsonResult selectAll(EnergyQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<Energy> page = query.buildPage(this);
        List<EnergyDTO> records = EntityUtil.parentListToChildList(page.getRecords(), EnergyDTO.class);
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
     * 根据主键删除
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
     * 根据主键修改
     *
     * @param record 修改信息
     * @return JsonResult
    */
    @Override
    public JsonResult updateByKey(EnergyVO record) {
        JsonResult jsonResult = new JsonResult();
        Energy updateData = new Energy();
        BeanUtils.copyProperties(record,updateData);
        if(updateById(updateData)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入
     *
     * @param record 插入信息
     * @return JsonResult
    */
    @Override
    public JsonResult add(EnergyVO record) {
        JsonResult jsonResult = new JsonResult();
        Energy addData = new Energy();
        BeanUtils.copyProperties(record,addData);
        if(save(addData)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
