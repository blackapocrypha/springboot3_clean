package com.deeeelete.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.system.entity.AclPath;
import com.deeeelete.system.entity.AclPathRole;
import com.deeeelete.system.entity.AclRole;

import com.deeeelete.system.entity.AclWhiteList;
import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.system.entity.enums.PathTypeEnum;
import com.deeeelete.system.entity.query.AclPathQuery;
import com.deeeelete.system.entity.dto.AclPathDTO;
import com.deeeelete.system.mapper.AclPathMapper;
import com.deeeelete.system.mapper.AclPathRoleMapper;
import com.deeeelete.system.mapper.AclWhiteListMapper;
import com.deeeelete.system.service.IAclPathRoleService;
import com.deeeelete.system.service.IAclPathService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.system.service.IAclRoleService;
import com.deeeelete.utils.EntityUtil;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.utils.constent.StaticStatusConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Service
public class AclPathServiceImpl extends ServiceImpl<AclPathMapper, AclPath> implements IAclPathService {

    @Resource
    private IAclPathRoleService aclPathRoleService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AclPathRoleMapper aclPathRoleMapper;

    @Resource
    private AclWhiteListMapper aclWhiteListMapper;


    @Value("${myConfig.token:N}")
    private String token;

    /**
     * 分页查询
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectAll(AclPathQuery query) {
        JsonResult jsonResult = new JsonResult();

        List<AclPath> aclPaths = list(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaPid, -1));
        if (ListUtil.isEmpty(aclPaths)) {
            jsonResult.buildFalse("无数据");
            return jsonResult;
        }

        /**
         * 取到根节点
         */
        AclPath aclPath = aclPaths.get(0);
        AclPathDTO rootData = EntityUtil.parentToChild(aclPath, AclPathDTO.class);
        findPathChild(rootData);

        if (StringUtil.isNotEmpty(rootData)) {
            jsonResult.buildTrue();
            jsonResult.setData(rootData);
        } else {
            jsonResult.buildFalse("无数据");
        }
        return jsonResult;
    }

    /**
     * 递归填充子类
     *
     * @param aclPathDTO
     */
    private void findPathChild(AclPathDTO aclPathDTO) {
        LambdaQueryWrapper<AclPath> findWrapper = new LambdaQueryWrapper<AclPath>()
                .eq(AclPath::getAcpaPid, aclPathDTO.getAcpaId()).orderByAsc(AclPath::getAcpaSort).orderByDesc(AclPath::getGmtCreate);
        List<AclPath> aclPaths = list(findWrapper);
        if (ListUtil.isNotEmpty(aclPaths)) {
            List<AclPathDTO> dto = EntityUtil.parentListToChildList(aclPaths, AclPathDTO.class);
            aclPathDTO.setChildren(dto);
            for (AclPathDTO aclPath : dto) {
                findPathChild(aclPath);
            }
        }
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
        if (id == 1L) {
            jsonResult.buildFalse("禁止删除根路径");
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
     * 根据主键修改
     *
     * @param record 修改信息
     * @return JsonResult
     */
    @Override
    public JsonResult updateByKey(AclPath record) {
        JsonResult jsonResult = new JsonResult();
        // 如果是接口
        if (record.getAcpaType().equals(PathTypeEnum.INTERFACE.getCode())) {
            if (StringUtil.isNotEmpty(record.getAcpaPid()) && count(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaId, record.getAcpaPid())) <= 0) {
                jsonResult.buildFalse("父类路径不存在");
                return jsonResult;
            }
            if (count(new LambdaQueryWrapper<AclPath>().ne(AclPath::getAcpaId, record.getAcpaId()).eq(AclPath::getAcpaPath, record.getAcpaPath())) > 0) {
                jsonResult.buildFalse("路径名不能重复");
                return jsonResult;
            }
        } else {
            // 如果是菜单，强制置空path
            record.setAcpaPath("");
        }


        if (updateById(record)) {
            // 更新缓存中的接口权限标志
            List<String> powers = new ArrayList<>();
            jsonResult.buildTrue();
            List<AclPathRole> pathRoles = aclPathRoleMapper.selectList(new LambdaQueryWrapper<AclPathRole>().eq(AclPathRole::getAcprPathId, record.getAcpaId()));
            for (AclPathRole pathRole : pathRoles) {
                if(!powers.contains(pathRole.getAcprRoleCode())){
                    powers.add(pathRole.getAcprRoleCode());
                }
            }
            redisTemplate.delete(record.getAcpaPath());
            redisTemplate.opsForValue().set(record.getAcpaPath(), JSON.toJSONString(powers));
        } else {
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
    public JsonResult add(AclPath record) {
        JsonResult jsonResult = new JsonResult();
        if (record.getAcpaType().equals(PathTypeEnum.INTERFACE.getCode())) {
            if (StringUtil.isEmpty(record.getAcpaName()) || StringUtil.isEmpty(record.getAcpaPath()) || StringUtil.isEmpty(record.getAcpaPid())) {
                jsonResult.buildFalse("必填项不能为空");
                return jsonResult;
            }
            if (count(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaPath, record.getAcpaPath())) > 0) {
                jsonResult.buildFalse("路径不能重复");
                return jsonResult;
            }
        } else {
            // 菜单则置空path
            record.setAcpaPath("");
            if (StringUtil.isEmpty(record.getAcpaName()) || StringUtil.isEmpty(record.getAcpaPid())) {
                jsonResult.buildFalse("必填项不能为空");
                return jsonResult;
            }
            if (count(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaName, record.getAcpaName())) > 0) {
                jsonResult.buildFalse("名称不能重复");
                return jsonResult;
            }
        }
        if (StringUtil.isNotEmpty(record.getAcpaPid())) {
            if (count(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaId, record.getAcpaPid())) <= 0) {
                jsonResult.buildFalse("父类路径不存在");
                return jsonResult;
            }
        }
        if (StringUtil.isEmpty(record.getAcpaSort())) {
            record.setAcpaSort(1);
        }

        if (save(record)) {
            List<String> powers = new ArrayList<>();
            jsonResult.buildTrue();
            redisTemplate.delete(record.getAcpaPath());
            redisTemplate.opsForValue().set(record.getAcpaPath(), JSON.toJSONString(powers));
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }

    /**
     * 初始化权限
     */
    @Override
    public void initPath() {
        // 初始化永久token
        redisTemplate.opsForValue().set("foreverToken",token);

        List<AclPath> aclPaths = list(new LambdaQueryWrapper<AclPath>().eq(AclPath::getAcpaType, 1));
        if (ListUtil.isEmpty(aclPaths)) {
            return;
        }

        List<Long> acIds = ListUtil.transformationByListToList(aclPaths, AclPath::getAcpaId);
        List<AclPathRole> aclPathRoles = aclPathRoleService.list(new LambdaQueryWrapper<AclPathRole>().in(AclPathRole::getAcprPathId, acIds));
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

        // 初始化白名单
        String whiteKey  = StaticStatusConfig.whiteKey;
        List<AclWhiteList> whiteLists = aclWhiteListMapper.selectList(null);
        for (AclWhiteList whiteList : whiteLists) {
            redisTemplate.opsForSet().add(whiteKey, whiteList.getAcwlPath());
        }

    }


}
