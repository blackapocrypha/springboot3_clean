package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclUserRoleDTO;
import com.deeeelete.system.entity.AclUserRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 角色用户绑定 查询类
    * </p>
*
* @author bin.xie
* @since 2023-05-30
*/
@Data
public class AclUserRoleQuery extends QueryParam {
    /**
    * 原类
    */
    private AclUserRoleDTO param;

    private QueryWrapper<AclUserRole> query;
    private UpdateWrapper<AclUserRole> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam












    public QueryWrapper<AclUserRole> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclUserRoleDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcurId())){
            query.eq("acur_id",getParam().getAcurId());
            update.eq("acur_id",getParam().getAcurId());
        }

        // 用户Id
        if(StringUtil.isNotEmpty(getParam().getAcurUserId())){
            query.eq("acur_user_id",getParam().getAcurUserId());
            update.eq("acur_user_id",getParam().getAcurUserId());
        }

        // 角色id
        if(StringUtil.isNotEmpty(getParam().getAcurRoleId())){
            query.eq("acur_role_id",getParam().getAcurRoleId());
            update.eq("acur_role_id",getParam().getAcurRoleId());
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
    public QueryWrapper<AclUserRole> getQueryP() {
        return query;
    }

}