package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclMenuRoleDTO;
import com.deeeelete.system.entity.AclMenuRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    *  查询类
    * </p>
*
* @author bin.xie
* @since 2023-06-24
*/
@Data
public class AclMenuRoleQuery extends QueryParam {
    /**
    * 原类
    */
    private AclMenuRoleDTO param;

    private QueryWrapper<AclMenuRole> query;
    private UpdateWrapper<AclMenuRole> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam






    // 角色名
    private String acmrRoleNameIsLike; 

    // 角色代码
    private String acmrRoleCodeIsLike; 

    public QueryWrapper<AclMenuRole> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclMenuRoleDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcmrId())){
            query.eq("acmr_id",getParam().getAcmrId());
            update.eq("acmr_id",getParam().getAcmrId());
        }

        // 菜单id
        if(StringUtil.isNotEmpty(getParam().getAcmrMenuId())){
            query.eq("acmr_menu_id",getParam().getAcmrMenuId());
            update.eq("acmr_menu_id",getParam().getAcmrMenuId());
        }

        // 角色id
        if(StringUtil.isNotEmpty(getParam().getAcmrRoleId())){
            query.eq("acmr_role_id",getParam().getAcmrRoleId());
            update.eq("acmr_role_id",getParam().getAcmrRoleId());
        }

        // 角色名
        if(StringUtil.isNotEmpty(getParam().getAcmrRoleName())){
            query.eq("acmr_role_name",getParam().getAcmrRoleName());
            update.eq("acmr_role_name",getParam().getAcmrRoleName());
        }
        if(StringUtil.isNotEmpty(getAcmrRoleNameIsLike())){
            query.like("acmr_role_name",getAcmrRoleNameIsLike());
            update.like("acmr_role_name",getAcmrRoleNameIsLike());
        }

        // 角色代码
        if(StringUtil.isNotEmpty(getParam().getAcmrRoleCode())){
            query.eq("acmr_role_code",getParam().getAcmrRoleCode());
            update.eq("acmr_role_code",getParam().getAcmrRoleCode());
        }
        if(StringUtil.isNotEmpty(getAcmrRoleCodeIsLike())){
            query.like("acmr_role_code",getAcmrRoleCodeIsLike());
            update.like("acmr_role_code",getAcmrRoleCodeIsLike());
        }

    //        // 开始时间
    //        if(StringUtil.isNotEmpty(getStartTime())){
    //            query.ge("create_time",getStartTime());
    //            update.ge("create_time",getStartTime());
    //        }
    //        // 结束时间
    //        if(StringUtil.isNotEmpty(getEndTime())){
    //            query.le("create_time",getEndTime());
    //            update.le("create_time",getEndTime());
    //        }

        // 排序
        if(StringUtil.isNotEmpty(getOrderItem())){
            if(StringUtil.isEmpty(getOrderType())){
                query.orderByAsc(Arrays.asList(getOrderItem().split(",")));
            }else if("asc".equals((getOrderType()))){
                query.orderByAsc(Arrays.asList(getOrderItem().split(",")));
            }else if("desc".equals((getOrderType()))){
                query.orderByDesc(Arrays.asList(getOrderItem().split(",")));
            }
        }

        return query;
    }



    @Override
    public QueryWrapper<AclMenuRole> getQueryP() {
        return query;
    }

}