package com.deeeelete.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.enums.RequestEnum;
import com.deeeelete.system.entity.AclPath;
import com.deeeelete.system.entity.AclPathRole;
import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.enums.PathTypeEnum;
import com.deeeelete.system.entity.query.AclPathRoleQuery;
import com.deeeelete.system.entity.dto.AclPathRoleDTO;
import com.deeeelete.system.mapper.AclPathMapper;
import com.deeeelete.system.mapper.AclPathRoleMapper;
import com.deeeelete.system.mapper.AclRoleMapper;
import com.deeeelete.system.service.IAclPathRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.system.service.IAclPathService;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  服务实现类
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Service
public class AclPathRoleServiceImpl extends ServiceImpl<AclPathRoleMapper, AclPathRole> implements IAclPathRoleService {

    @Autowired
    private AclRoleMapper aclRoleMapper;

    @Autowired
    private AclPathMapper aclPathMapper;
    
    @Autowired
    private RedisTemplate redisTemplate;
    

    /**
    * 分页查询
    *
    * @return JsonResult
    */
    @Override
    public JsonResult selectAll(AclPathRoleQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<AclPathRole> page = query.buildPage(this);
        List<AclPathRoleDTO> records = EntityUtil.parentListToChildList(page.getRecords(), AclPathRoleDTO.class);
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
     * 根据角色id查询接口
     *
     * @param id
     * @return JsonResult
     */
    @Override
    public JsonResult selectPathByRoleId(Long id) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtil.isEmpty(id)){
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        List<AclPathRole> aclPathRoles = list(new LambdaQueryWrapper<AclPathRole>().eq(AclPathRole::getAcprRoleId, id));
        if(ListUtil.isEmpty(aclPathRoles)){
            jsonResult.buildFalse("无数据");
            return jsonResult;
        }

        List<Long> ids = aclPathRoles.stream().map(m -> m.getAcprPathId()).collect(Collectors.toList());
        List<AclPath> aclPaths = aclPathMapper.selectList(new LambdaQueryWrapper<AclPath>().in(AclPath::getAcpaId, ids).eq(AclPath::getAcpaType, PathTypeEnum.INTERFACE.getCode()));

        if(ListUtil.isNotEmpty(aclPaths)){
            jsonResult.buildTrue();
            jsonResult.setData(aclPaths);
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
            refreshPath();
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
    @Transactional
    public JsonResult updateByKey(AclPathRoleDTO record) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtil.isEmpty(record.getAcprPathId())){
            jsonResult.buildFalse("路径不能为空");
            return jsonResult;
        }

        if(updateById(record)){
            // 清空旧的，插入新的
            remove(new LambdaQueryWrapper<AclPathRole>().eq(AclPathRole::getAcprPathId,record.getAcprPathId()));
            String[] split = record.getRoleIds().split(",");
            if(split.length>0){
                List<Long> roleIds = Arrays.asList(split).stream().map(m -> Long.parseLong(m)).distinct().collect(Collectors.toList());
                List<AclRole> pathRoles = aclRoleMapper.selectList(new LambdaQueryWrapper<AclRole>().in(AclRole::getRoleId, roleIds));
                Map<Long, AclRole> roelMap = ListUtil.transformationByListToMapObject(pathRoles, AclRole::getRoleId);

                // 准备插入
                List<AclPathRole> prepareInsert = new ArrayList<>();
                for (Long roleId : roleIds) {
                    AclRole role = roelMap.get(roleId);
                    AclPathRole aclPathRole = new AclPathRole();
                    aclPathRole.setAcprRoleName(role.getRoleName()).setAcprRoleCode(role.getRoleCode()).setAcprRoleId(role.getRoleId()).setAcprPathId(record.getAcprPathId());
                    prepareInsert.add(aclPathRole);
                }
                saveBatch(prepareInsert);
            }

            jsonResult.buildTrue();
            refreshPath();
        }else{
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 根据角色修改接口
     *
     * @param record 相关信息
     * @return JsonResult
     */
    @Override
    @Transactional
    public JsonResult updatePathByRole(AclPathRoleDTO record) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtil.isEmpty(record.getAcprRoleId()) ){
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        // 当前角色
        AclRole role = aclRoleMapper.selectById(record.getAcprRoleId());
        // 移除旧关联
        remove(new LambdaQueryWrapper<AclPathRole>().eq(AclPathRole::getAcprRoleId,record.getAcprRoleId()));

        List<Long> pathIdList = new ArrayList<>();
        try {
            if(StringUtil.isEmpty(record.getPathIds())){
                jsonResult.buildTrue();
                return jsonResult;
            }
            String[] split = record.getPathIds().split(",");
            pathIdList = Arrays.asList(split).stream().map(m -> Long.parseLong(m)).collect(Collectors.toList());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            jsonResult.buildFalse("格式错误");
            return jsonResult;
        }


        // 去掉菜单的关联
        List<AclPathRole> prepareInsert = new ArrayList<>();
        List<AclPath> aclPaths = aclPathMapper.selectList(new LambdaQueryWrapper<AclPath>().in(AclPath::getAcpaId, pathIdList));
        Map<Long, AclPath> pathMap = ListUtil.transformationByListToMapObject(aclPaths, AclPath::getAcpaId);
        for (Long pathId : pathIdList) {
            if (pathMap.get(pathId).getAcpaType().equals(PathTypeEnum.INTERFACE.getCode())) {
                AclPathRole aclPathRole = new AclPathRole();
                aclPathRole.setAcprPathId(pathId);
                aclPathRole.setAcprRoleId(role.getRoleId());
                aclPathRole.setAcprRoleCode(role.getRoleCode());
                aclPathRole.setAcprRoleName(role.getRoleName());
                prepareInsert.add(aclPathRole);
            }
        }

        if(saveBatch(prepareInsert)){
            jsonResult.buildTrue();
            refreshPath();
        }else{
            jsonResult.buildFalse(RequestEnum.REQUEST_ERROR_DATABASE_INSERT_ERROR);
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
    public JsonResult add(AclPathRoleDTO record) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtil.isEmpty(record.getAcprPathId()) || StringUtil.isEmpty(record.getRoleIds()) ){
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        List<AclPathRole> aclPathRoles = list(new LambdaQueryWrapper<AclPathRole>().eq(AclPathRole::getAcprPathId,record.getAcprPathId()));
        String[] split = record.getRoleIds().split(",");
        List<Long> roleIds = Arrays.asList(split).stream().map(m -> Long.parseLong(m)).distinct().collect(Collectors.toList());
        for (AclPathRole aclPathRole : aclPathRoles) {
            if(roleIds.contains(aclPathRole.getAcprRoleId())){
                jsonResult.buildFalse("禁止重复添加权限");
                return jsonResult;
            }
        }

        List<AclRole> pathRoles = aclRoleMapper.selectList(new LambdaQueryWrapper<AclRole>().in(AclRole::getRoleId, roleIds));
        Map<Long, AclRole> roelMap = ListUtil.transformationByListToMapObject(pathRoles, AclRole::getRoleId);

        // 准备插入
        List<AclPathRole> prepareInsert = new ArrayList<>();
        for (Long roleId : roleIds) {
            AclRole role = roelMap.get(roleId);
            AclPathRole aclPathRole = new AclPathRole();
            aclPathRole.setAcprRoleName(role.getRoleName()).setAcprRoleCode(role.getRoleCode()).setAcprRoleId(role.getRoleId()).setAcprPathId(record.getAcprPathId());
            prepareInsert.add(aclPathRole);
        }

        if(saveBatch(prepareInsert)){
            jsonResult.buildTrue();
            refreshPath();
        }else{
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
        List<AclPathRole> aclPathRoles = list(new LambdaQueryWrapper<AclPathRole>().in(AclPathRole::getAcprPathId, acIds));
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
