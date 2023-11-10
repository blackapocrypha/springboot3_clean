package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclPathRoleDTO;
import com.deeeelete.system.entity.AclPathRole;
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
* @since 2023-05-30
*/
@Data
public class AclPathRoleQuery extends QueryParam {
    /**
    * 原类
    */
    private AclPathRoleDTO param;

    private QueryWrapper<AclPathRole> query;
    private UpdateWrapper<AclPathRole> update;

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
    private String acprRoleNameIsLike; 

    // 角色代码
    private String acprRoleCodeIsLike; 

    public QueryWrapper<AclPathRole> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclPathRoleDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcprId())){
            query.eq("acpr_id",getParam().getAcprId());
            update.eq("acpr_id",getParam().getAcprId());
        }

        // 路径id
        if(StringUtil.isNotEmpty(getParam().getAcprPathId())){
            query.eq("acpr_path_id",getParam().getAcprPathId());
            update.eq("acpr_path_id",getParam().getAcprPathId());
        }

        // 角色id
        if(StringUtil.isNotEmpty(getParam().getAcprRoleId())){
            query.eq("acpr_role_id",getParam().getAcprRoleId());
            update.eq("acpr_role_id",getParam().getAcprRoleId());
        }

        // 角色名
        if(StringUtil.isNotEmpty(getParam().getAcprRoleName())){
            query.eq("acpr_role_name",getParam().getAcprRoleName());
            update.eq("acpr_role_name",getParam().getAcprRoleName());
        }
        if(StringUtil.isNotEmpty(getAcprRoleNameIsLike())){
            query.like("acpr_role_name",getAcprRoleNameIsLike());
            update.like("acpr_role_name",getAcprRoleNameIsLike());
        }

        // 角色代码
        if(StringUtil.isNotEmpty(getParam().getAcprRoleCode())){
            query.eq("acpr_role_code",getParam().getAcprRoleCode());
            update.eq("acpr_role_code",getParam().getAcprRoleCode());
        }
        if(StringUtil.isNotEmpty(getAcprRoleCodeIsLike())){
            query.like("acpr_role_code",getAcprRoleCodeIsLike());
            update.like("acpr_role_code",getAcprRoleCodeIsLike());
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
    public QueryWrapper<AclPathRole> getQueryP() {
        return query;
    }

}