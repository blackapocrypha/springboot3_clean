package com.deeeelete.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.system.entity.AclMenu;
import com.deeeelete.system.entity.AclMenuRole;
import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.system.entity.query.AclMenuQuery;
import com.deeeelete.system.entity.dto.AclMenuDTO;
import com.deeeelete.system.mapper.AclMenuMapper;
import com.deeeelete.system.mapper.AclMenuRoleMapper;
import com.deeeelete.system.service.IAclMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 服务实现类
 *
 * @author bin.xie
 * @since 2023-06-24
 */
@Service
public class AclMenuServiceImpl extends ServiceImpl<AclMenuMapper, AclMenu> implements IAclMenuService {

    @Autowired
    private AclMenuRoleMapper aclMenuRoleMapper;

    /**
     * 分页查询菜单
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectAll(AclMenuQuery query) {
        JsonResult jsonResult = new JsonResult();
        if(query.getIsTree().equals(EnableEnum.STATUS_NO_PROCESS.getCode())){
            jsonResult.buildTrue();
            jsonResult.setData(list());
            return jsonResult;
        }
        // 找出根节点
        AclMenuDTO rootMenu = EntityUtil.parentToChild(getById(1), AclMenuDTO.class);
        List<AclMenuDTO> allMenus = EntityUtil.parentListToChildList(list(), AclMenuDTO.class);
        makeRootCildren(rootMenu, allMenus);
        jsonResult.buildTrue();
        jsonResult.setData(rootMenu);
        return jsonResult;
    }


    /**
     * 递归塞入子菜单
     *
     * @param menu 菜单
     */
    public void makeRootCildren(AclMenuDTO menu, List<AclMenuDTO> allMenus) {
        if (StringUtil.isEmpty(menu)) {
            return;
        }
        List<AclMenuDTO> children = allMenus
                .stream()
                .filter(f -> f.getAcmePid().equals(menu.getAcmeId()))
                .sorted(Comparator.comparing(AclMenuDTO::getAcmeSort))
                .collect(Collectors.toList());
        menu.setChildren(children);
        if (ListUtil.isNotEmpty(children)) {
            for (AclMenuDTO aclMenuDTO : children) {
                makeRootCildren(aclMenuDTO, allMenus);
            }
        }
    }

    /**
     * 根据主键删除菜单
     *
     * @param id 主键
     * @return JsonResult
     */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        if (id != 1 && removeById(id)) {
            jsonResult.buildTrue();
            // 同时递归删除当前菜单下的所有子菜单
            deleteChild(id);
        } else {
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 递归删除所有子节点
     * @param id 关键id
     */
    private void deleteChild(Long id){
        aclMenuRoleMapper.delete(new LambdaQueryWrapper<AclMenuRole>().eq(AclMenuRole::getAcmrMenuId,id));
        LambdaQueryWrapper<AclMenu> deleteWrapper = new LambdaQueryWrapper<AclMenu>().eq(AclMenu::getAcmePid, id);
        if(count(deleteWrapper)>0){
            List<AclMenu> list = list(deleteWrapper);
            remove(deleteWrapper);
            for (AclMenu aclMenu : list) {
                deleteChild(aclMenu.getAcmeId());
            }
        }
    }

    /**
     * 根据主键修改菜单
     *
     * @param record 修改信息
     * @return JsonResult
     */
    @Override
    public JsonResult updateByKey(AclMenu record) {
        JsonResult jsonResult = new JsonResult();
        if (updateById(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入菜单
     *
     * @param record 插入信息
     * @return JsonResult
     */
    @Override
    public JsonResult add(AclMenu record) {
        JsonResult jsonResult = new JsonResult();
        record.setAcmeId(null);

        if(StringUtil.isEmpty(record.getAcmeName()) || StringUtil.isEmpty(record.getAcmeCode()) || StringUtil.isEmpty(record.getAcmePid()) || StringUtil.isEmpty(record.getAcmeType())){
            jsonResult.buildFalse("必填项不能为空");
            return jsonResult;
        }
        if(StringUtil.isEmpty(record.getAcmeSort())){
            record.setAcmeSort(1);
        }

        if(count(new LambdaQueryWrapper<AclMenu>().eq(AclMenu::getAcmeCode,record.getAcmeCode()))>0){
            jsonResult.buildFalse("权限值不能重复");
            return jsonResult;
        }

        if (save(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
