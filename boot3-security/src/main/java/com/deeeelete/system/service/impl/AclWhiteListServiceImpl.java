package com.deeeelete.system.service.impl;

import com.deeeelete.system.entity.AclWhiteList;
import com.deeeelete.system.entity.query.AclWhiteListQuery;
import com.deeeelete.system.entity.dto.AclWhiteListDTO;
import com.deeeelete.system.mapper.AclWhiteListMapper;
import com.deeeelete.system.service.IAclWhiteListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.utils.constent.StaticStatusConfig;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * 白名单 服务实现类
 *
 * @author
 * @since 2024-01-25
 */
@Service
public class AclWhiteListServiceImpl extends ServiceImpl<AclWhiteListMapper, AclWhiteList> implements IAclWhiteListService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
    * 分页查询白名单
    *
    * @return JsonResult
    */
    @Override
    public JsonResult selectAll(AclWhiteListQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<AclWhiteList> page = query.buildPage(this);
        List<AclWhiteListDTO> records = EntityUtil.parentListToChildList(page.getRecords(), AclWhiteListDTO.class);
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
     * 根据主键删除白名单
     *
     * @param id 主键
     * @return JsonResult
    */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        AclWhiteList whiteList = getById(id);
        if(StringUtil.isEmpty(whiteList)){
            return jsonResult.buildFalse("id不存在");
        }
        if(whiteList.getAcwlLock()==1){
            return jsonResult.buildFalse("不允许删除默认白名单");
        }

        if(removeById(id)){
            jsonResult.buildTrue();
            redisTemplate.opsForSet().remove(StaticStatusConfig.whiteKey,whiteList.getAcwlPath());
        }else{
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改白名单
     *
     * @param record 修改信息
     * @return JsonResult
    */
    @Override
    public JsonResult updateByKey(AclWhiteList record) {
        JsonResult jsonResult = new JsonResult();
        AclWhiteList oldList = getById(record.getAcwlId());
        if(StringUtil.isEmpty(oldList)){
            return jsonResult.buildFalse("数据不存在");
        }
        if(oldList.getAcwlLock() == 1){
            return jsonResult.buildFalse("不允许更新固定内容");
        }
        if(updateById(record)){
            // 当接口地址发生变更则更新缓存
            if(StringUtil.isNotEmpty(record.getAcwlPath()) && !oldList.getAcwlPath().equals(record.getAcwlPath())){
                redisTemplate.opsForSet().remove(StaticStatusConfig.whiteKey,oldList.getAcwlPath());
                redisTemplate.opsForSet().add(StaticStatusConfig.whiteKey,record.getAcwlPath());
            }
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入白名单
     *
     * @param record 插入信息
     * @return JsonResult
    */
    @Override
    public JsonResult add(AclWhiteList record) {
        JsonResult jsonResult = new JsonResult();
        if(save(record)){
            redisTemplate.opsForSet().add(StaticStatusConfig.whiteKey,record.getAcwlPath());
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
