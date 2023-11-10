package com.deeeelete.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.system.entity.*;
import com.deeeelete.system.entity.query.AclRoleQuery;
import com.deeeelete.system.entity.dto.AclRoleDTO;
import com.deeeelete.system.mapper.*;
import com.deeeelete.system.service.IAclRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色表 服务实现类
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements IAclRoleService {

    @Autowired
    private AclPathRoleMapper aclPathRoleMapper;
    @Autowired
    private AclMenuRoleMapper aclMenuRoleMapper;
    @Autowired
    private AclRoleMapper aclRoleMapper;

    @Autowired
    private AclPathMapper aclPathMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 分页查询角色表
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectAll(AclRoleQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<AclRole> page = query.buildPage(this);
        List<AclRoleDTO> records = EntityUtil.parentListToChildList(page.getRecords(), AclRoleDTO.class);
        if (ListUtil.isNotEmpty(records)) {
            jsonResult.buildTrue();
            jsonResult.setData(records);
            jsonResult.setTotalsize(page.getTotal());
        } else {
            jsonResult.buildFalse("无数据");
        }
        return jsonResult;
    }

    /**
     * 根据主键删除角色表
     *
     * @param id 主键
     * @return JsonResult
     */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(id)) {
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }
        if (id == 1L) {
            jsonResult.buildFalse("禁止删除默认角色");
            return jsonResult;
        }

        if (removeById(id)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改角色表
     *
     * @param record 修改信息
     * @return JsonResult
     */
    @Override
    public JsonResult updateByKey(AclRole record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getRoleId())) {
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        if(count(new LambdaQueryWrapper<AclRole>().ne(AclRole::getRoleId,record.getRoleId())
                .eq(AclRole::getRoleCode,record.getRoleCode()))>0){
            jsonResult.buildFalse("角色代码不能重复");
            return jsonResult;
        }
        if(count(new LambdaQueryWrapper<AclRole>().ne(AclRole::getRoleId,record.getRoleId())
                .eq(AclRole::getRoleName,record.getRoleName()))>0){
            jsonResult.buildFalse("角色名不能重复");
            return jsonResult;
        }

        AclRole oldRole = getById(record.getRoleId());

        if (updateById(record)) {
            // 同步更新其他表中的数据
            if(!oldRole.getRoleCode().equals(record.getRoleCode()) && StringUtil.isNotEmpty(record.getRoleCode())){
                AclPathRole aclPathRole = new AclPathRole();
                aclPathRole.setAcprRoleCode(record.getRoleCode());
                AclMenuRole aclMenuRole = new AclMenuRole();
                aclMenuRole.setAcmrRoleCode(record.getRoleCode());
                aclPathRoleMapper
                        .update(aclPathRole,new LambdaQueryWrapper<AclPathRole>()
                                .eq(AclPathRole::getAcprRoleId,record.getRoleId()));
                aclMenuRoleMapper.update(aclMenuRole,new LambdaQueryWrapper<AclMenuRole>()
                        .eq(AclMenuRole::getAcmrRoleId,record.getRoleId()));
                // 刷新缓存
                refreshPath();
            }
            if(!oldRole.getRoleName().equals(record.getRoleName()) && StringUtil.isNotEmpty(record.getRoleName())){
                AclPathRole aclPathRole = new AclPathRole();
                aclPathRole.setAcprRoleName(record.getRoleName());
                AclMenuRole aclMenuRole = new AclMenuRole();
                aclMenuRole.setAcmrRoleName(record.getRoleName());
                aclPathRoleMapper
                        .update(aclPathRole,new LambdaQueryWrapper<AclPathRole>()
                                .eq(AclPathRole::getAcprRoleId,record.getRoleId()));
                aclMenuRoleMapper.update(aclMenuRole,new LambdaQueryWrapper<AclMenuRole>()
                        .eq(AclMenuRole::getAcmrRoleId,record.getRoleId()));
            }

            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入角色表
     *
     * @param record 插入信息
     * @return JsonResult
     */
    @Override
    public JsonResult add(AclRole record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getRoleCode()) || StringUtil.isEmpty(record.getRoleName())) {
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        if (count(new LambdaQueryWrapper<AclRole>().eq(AclRole::getRoleCode, record.getRoleCode())) > 0) {
            jsonResult.buildFalse("编号代码不能重复");
            return jsonResult;
        }

        if (save(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }



    /**
     * 刷新缓存中的权限
     */
    private void refreshPath(){
        List<AclPath> aclPaths = aclPathMapper.selectList(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaType, 1));
        if (ListUtil.isEmpty(aclPaths)) {
            return;
        }

        List<Long> acIds = ListUtil.transformationByListToList(aclPaths, AclPath::getAcpaId);
        List<AclPathRole> aclPathRoles = aclPathRoleMapper.selectList(new LambdaQueryWrapper<AclPathRole>().in(AclPathRole::getAcprPathId, acIds));
        if (ListUtil.isEmpty(aclPathRoles)) {
            return;
        }


        Map<Long, List<AclPathRole>> byListToMapList = ListUtil.transformationByListToMapList(aclPathRoles, AclPathRole::getAcprPathId);
        for (AclPath aclPath : aclPaths) {
            List<String> powers = new ArrayList<>();
            List<AclPathRole> pathRoles = byListToMapList.get(aclPath.getAcpaId());
            redisTemplate.delete(aclPath.getAcpaPath());
            if (ListUtil.isNotEmpty(pathRoles)) {
                for (AclPathRole pathRole : pathRoles) {
                    powers.add(pathRole.getAcprRoleCode());
                }
                redisTemplate.opsForValue().set(aclPath.getAcpaPath(), JSON.toJSONString(powers));
            } else {
                redisTemplate.opsForValue().set(aclPath.getAcpaPath(), JSON.toJSONString(powers));
            }
        }
    }
}
