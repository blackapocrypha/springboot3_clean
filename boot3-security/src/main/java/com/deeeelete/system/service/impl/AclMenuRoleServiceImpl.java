package com.deeeelete.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.enums.RequestEnum;
import com.deeeelete.system.entity.*;
import com.deeeelete.system.entity.enums.MenuTypeEnum;
import com.deeeelete.system.entity.query.AclMenuRoleQuery;
import com.deeeelete.system.entity.dto.AclMenuRoleDTO;
import com.deeeelete.system.mapper.*;
import com.deeeelete.system.service.IAclMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务实现类
 *
 * @author bin.xie
 * @since 2023-06-24
 */
@Service
public class AclMenuRoleServiceImpl extends ServiceImpl<AclMenuRoleMapper, AclMenuRole> implements IAclMenuRoleService {

    @Autowired
    private AclUserMapper aclUserMapper;

    @Autowired
    private AclUserRoleMapper aclUserRoleMapper;

    @Autowired
    private AclMenuRoleMapper aclMenuRoleMapper;

    @Autowired
    private AclMenuMapper aclMenuMapper;

    @Autowired
    private AclRoleMapper aclRoleMapper;

    /**
     * 分页查询
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectCurrentUserMenu(Long id) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(id) || aclUserMapper.selectCount(new LambdaQueryWrapper<AclUser>().eq(AclUser::getId, id)) <= 0) {
            jsonResult.buildFalse("用户不存在");
            return jsonResult;
        }
        List<AclUserRole> aclUserRoleList = aclUserRoleMapper.selectList(new LambdaQueryWrapper<AclUserRole>().eq(AclUserRole::getAcurUserId, id));
        if (ListUtil.isEmpty(aclUserRoleList)) {
            jsonResult.buildFalse("用户无角色");
            return jsonResult;
        }

        List<Long> roleIds = aclUserRoleList.stream().map(m -> m.getAcurRoleId()).collect(Collectors.toList());
        List<AclMenuRole> aclMenuRoles = aclMenuRoleMapper
                .selectList(new LambdaQueryWrapper<AclMenuRole>()
                        .in(AclMenuRole::getAcmrRoleId, roleIds));

        // 去掉重复的菜单
        ArrayList<Long> idList = new ArrayList<>();

        if (ListUtil.isNotEmpty(aclMenuRoles)) {
            for (AclMenuRole aclMenuRole : aclMenuRoles) {
                if (!idList.contains(aclMenuRole.getAcmrMenuId())) {
                    idList.add(aclMenuRole.getAcmrMenuId());
                }
            }
            jsonResult.buildTrue();
            jsonResult.setData(aclMenuMapper.selectList(new LambdaQueryWrapper<AclMenu>().orderByAsc(AclMenu::getAcmeSort).in(AclMenu::getAcmeId, idList)));
        } else {
            jsonResult.buildFalse(RequestEnum.REQUEST_ERROR_DATABASE_QUERY_NO_DATA);
        }

        return jsonResult;
    }

    /**
     * 根据角色查询菜单
     *
     * @param id 角色id
     * @return JsonResult
     */
    @Override
    public JsonResult selectCurrentRoleMenu(Long id) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(id) || aclRoleMapper.selectCount(new LambdaQueryWrapper<AclRole>().eq(AclRole::getRoleId, id)) <= 0) {
            jsonResult.buildFalse("角色不存在");
            return jsonResult;
        }

        // 获取角色下的所有菜单
        List<AclMenuRole> aclMenuRoles = aclMenuRoleMapper.selectList(new LambdaQueryWrapper<AclMenuRole>().eq(AclMenuRole::getAcmrRoleId, id));
        if (ListUtil.isEmpty(aclMenuRoles)) {
            jsonResult.buildFalse(RequestEnum.REQUEST_ERROR_DATABASE_QUERY_NO_DATA);
            return jsonResult;
        }

        List<Long> menuIds = ListUtil.transformationByListToList(aclMenuRoles, AclMenuRole::getAcmrMenuId);
        List<AclMenu> menus = aclMenuMapper.selectList(new LambdaQueryWrapper<AclMenu>().orderByAsc(AclMenu::getAcmeSort).in(AclMenu::getAcmeId, menuIds));
        jsonResult.buildTrue();
        jsonResult.setData(filteMenu(menus));
        return jsonResult;
    }

    /**
     * 过滤菜单，对存在子类的菜单不传递父类，对只有父类的才专递父类
     *
     * @param menus 菜单
     * @return ArrayList<AclMenu>
     */
    public ArrayList<AclMenu> filteMenu(List<AclMenu> menus) {
        ArrayList<AclMenu> filteMenu = new ArrayList<>();

        for (AclMenu menu : menus) {
            if (StringUtil.isNotEmpty(menu.getAcmeType()) && menu.getAcmeType().equals(MenuTypeEnum.BUTTON)) {
                filteMenu.add(menu);
                continue;
            } else {
                // 是菜单，查看旗下是否有对应的子类载入
                List<AclMenu> children = menus.stream().filter(f -> f.getAcmePid().equals(menu.getAcmeId())).collect(Collectors.toList());
                if (ListUtil.isEmpty(children)) {
                    filteMenu.add(menu);
                }
            }
        }
        return filteMenu;
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
        if (removeById(id)) {
            jsonResult.buildTrue();
        } else {
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
    public JsonResult updateByKey(AclMenuRoleDTO record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getAcmrRoleId())) {
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }

        // 没有菜单，说明清空当前角色的所有菜单
        if (StringUtil.isEmpty(record.getMenuIds())) {
            remove(new LambdaQueryWrapper<AclMenuRole>().eq(AclMenuRole::getAcmrRoleId, record.getAcmrRoleId()));
            jsonResult.buildTrue();
            return jsonResult;
        }


        List<Long> idList = null;
        try {
            String[] split = record.getMenuIds().split(",");
            idList = Arrays.asList(split).stream().map(m -> Long.parseLong(m)).collect(Collectors.toList());
        } catch (Exception e) {
            jsonResult.buildFalse("id格式错误");
            return jsonResult;
        }

        List<AclMenu> allMenus = aclMenuMapper.selectList(null);
        Map<Long, AclMenu> menuMap = ListUtil.transformationByListToMapObject(allMenus, AclMenu::getAcmeId);
        List<Long> realIdList = new ArrayList<>();
        for (Long id : idList) {
            findAllParentId(realIdList,id,menuMap);
        }
        remove(new LambdaQueryWrapper<AclMenuRole>().eq(AclMenuRole::getAcmrRoleId, record.getAcmrRoleId()));
        List<AclMenuRole> aclMenuRoles = new ArrayList<>();
        AclRole currentRole = aclRoleMapper.selectById(record.getAcmrRoleId());
        for (Long menuId : realIdList) {
            AclMenuRole aclMenuRole = new AclMenuRole();
            aclMenuRole.setAcmrRoleId(record.getAcmrRoleId());
            if (StringUtil.isNotEmpty(currentRole)) {
                aclMenuRole.setAcmrRoleCode(currentRole.getRoleCode());
                aclMenuRole.setAcmrRoleName(currentRole.getRoleName());
            }
            aclMenuRole.setAcmrMenuId(menuId);
            aclMenuRoles.add(aclMenuRole);
        }

        if (saveBatch(aclMenuRoles)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("更新失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    /**
     * 找到按钮的上级菜单
     * @param idList
     * @param id
     * @param menuMap
     */
    private void findAllParentId(List<Long> idList, Long id, Map<Long, AclMenu> menuMap) {
        if (!idList.contains(id) &&  id!=0L){
            idList.add(id);
        }

        AclMenu aclMenu = menuMap.get(id);
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(aclMenu)) {
            return;
        }

        if (!idList.contains(aclMenu.getAcmePid()) && aclMenu.getAcmePid()!=0L) {
            idList.add(aclMenu.getAcmePid());
            findAllParentId(idList,aclMenu.getAcmePid(),menuMap);
        }
    }

    /**
     * 插入
     *
     * @param record 插入信息
     * @return JsonResult
     */
    @Override
    public JsonResult add(AclMenuRole record) {
        JsonResult jsonResult = new JsonResult();
        if (save(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
