package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclRoleDTO;
import com.deeeelete.system.entity.AclRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 角色表 查询类
    * </p>
*
* @author bin.xie
* @since 2023-05-30
*/
@Data
public class AclRoleQuery extends QueryParam {
    /**
    * 原类
    */
    private AclRoleDTO param;

    private QueryWrapper<AclRole> query;
    private UpdateWrapper<AclRole> update;

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
    private String roleNameIsLike; 

    // 代码
    private String roleCodeIsLike; 

    // 备注
    private String roleRemarkIsLike; 







    public QueryWrapper<AclRole> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclRoleDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getRoleId())){
            query.eq("role_id",getParam().getRoleId());
            update.eq("role_id",getParam().getRoleId());
        }

        // 角色名
        if(StringUtil.isNotEmpty(getParam().getRoleName())){
            query.eq("role_name",getParam().getRoleName());
            update.eq("role_name",getParam().getRoleName());
        }
        if(StringUtil.isNotEmpty(getRoleNameIsLike())){
            query.like("role_name",getRoleNameIsLike());
            update.like("role_name",getRoleNameIsLike());
        }

        // 代码
        if(StringUtil.isNotEmpty(getParam().getRoleCode())){
            query.eq("role_code",getParam().getRoleCode());
            update.eq("role_code",getParam().getRoleCode());
        }
        if(StringUtil.isNotEmpty(getRoleCodeIsLike())){
            query.like("role_code",getRoleCodeIsLike());
            update.like("role_code",getRoleCodeIsLike());
        }

        // 备注
        if(StringUtil.isNotEmpty(getParam().getRoleRemark())){
            query.eq("role_remark",getParam().getRoleRemark());
            update.eq("role_remark",getParam().getRoleRemark());
        }
        if(StringUtil.isNotEmpty(getRoleRemarkIsLike())){
            query.like("role_remark",getRoleRemarkIsLike());
            update.like("role_remark",getRoleRemarkIsLike());
        }

        // 创建时间
        if(StringUtil.isNotEmpty(getParam().getGmtCreate())){
            query.eq("gmt_create",getParam().getGmtCreate());
            update.eq("gmt_create",getParam().getGmtCreate());
        }

        // 更新时间
        if(StringUtil.isNotEmpty(getParam().getGmtModified())){
            query.eq("gmt_modified",getParam().getGmtModified());
            update.eq("gmt_modified",getParam().getGmtModified());
        }

        // 逻辑删除 0否 1是
        if(StringUtil.isNotEmpty(getParam().getIsDeleted())){
            query.eq("is_deleted",getParam().getIsDeleted());
            update.eq("is_deleted",getParam().getIsDeleted());
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
    public QueryWrapper<AclRole> getQueryP() {
        return query;
    }

}