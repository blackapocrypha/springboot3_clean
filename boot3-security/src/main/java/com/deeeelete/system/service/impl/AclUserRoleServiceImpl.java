package com.deeeelete.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.AclUserRole;
import com.deeeelete.system.entity.convert.AclUserRoleMapping;
import com.deeeelete.system.entity.query.AclUserRoleQuery;
import com.deeeelete.system.entity.dto.AclUserRoleDTO;
import com.deeeelete.system.mapper.AclRoleMapper;
import com.deeeelete.system.mapper.AclUserRoleMapper;
import com.deeeelete.system.service.IAclRoleService;
import com.deeeelete.system.service.IAclUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色用户绑定 服务实现类
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Service
public class AclUserRoleServiceImpl extends ServiceImpl<AclUserRoleMapper, AclUserRole> implements IAclUserRoleService {


    /**
     * 分页查询角色用户绑定
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectAll(AclUserRoleQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<AclUserRole> page = query.buildPage(this);
        List<AclUserRoleDTO> records = EntityUtil.parentListToChildList(page.getRecords(), AclUserRoleDTO.class);
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
     * 根据主键删除角色用户绑定
     *
     * @param id 主键
     * @return JsonResult
     */
    @Override
    public JsonResult deleteByKey(Long userId, Long roleId) {
        JsonResult jsonResult = new JsonResult();
        LambdaQueryWrapper<AclUserRole> removeWrapper = new LambdaQueryWrapper<AclUserRole>()
                .eq(AclUserRole::getAcurRoleId, roleId)
                .eq(AclUserRole::getAcurUserId, userId);
        if (count(removeWrapper) <= 0) {
            jsonResult.buildFalse("用户不存在该角色");
            return jsonResult;
        }

        if (remove(removeWrapper)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改角色用户绑定
     *
     * @param record 修改信息
     * @return JsonResult
     */
    @Override
    public JsonResult updateByKey(AclUserRoleDTO aclUserRoleDTO) {
        AclUserRole record = AclUserRoleMapping.INSTANCE.toPO(aclUserRoleDTO);
        JsonResult jsonResult = new JsonResult();
        if (updateById(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入角色用户绑定
     *
     * @param record 插入信息
     * @return JsonResult
     */
    @Override
    @Transactional
    public JsonResult add(AclUserRoleDTO record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getAcurUserId())) {
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        AclUserRole userRole = EntityUtil.childToParent(record, AclUserRole.class);

        if (StringUtil.isEmpty(record.getAllRoles())) {
            if (StringUtil.isEmpty(record.getAcurRoleId())) {
                jsonResult.buildFalse("角色不能为空");
                return jsonResult;
            }

            if (count(new LambdaQueryWrapper<AclUserRole>()
                    .eq(AclUserRole::getAcurRoleId, record.getAcurRoleId())
                    .eq(AclUserRole::getAcurUserId, record.getAcurUserId())) > 0) {
                jsonResult.buildFalse("已拥有该角色，不能重复添加");
                return jsonResult;
            }
        } else {
            List<Long> idList = null;
            List<AclUserRole> userRoles = new ArrayList<>();
            // 多个角色插入
            try {
                String[] split = record.getAllRoles().split(",");
                idList = Arrays.asList(split).stream().map(m -> Long.parseLong(m)).collect(Collectors.toList());
            } catch (Exception e) {
                jsonResult.buildFalse("id错误");
                return jsonResult;
            }

            // 移除原有的全部角色
            remove(new LambdaQueryWrapper<AclUserRole>()
                    .eq(AclUserRole::getAcurUserId, record.getAcurUserId()));

            // 角色有变，批量插入
            for (Long roleId : idList) {
                AclUserRole aclUserRole = new AclUserRole();
                aclUserRole.setAcurRoleId(roleId);
                aclUserRole.setAcurUserId(record.getAcurUserId());
                userRoles.add(aclUserRole);
            }

            if (saveBatch(userRoles)) {
                jsonResult.buildTrue();
            } else {
                jsonResult.buildFalse("插入失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            return jsonResult;

        }


        if (save(userRole)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
