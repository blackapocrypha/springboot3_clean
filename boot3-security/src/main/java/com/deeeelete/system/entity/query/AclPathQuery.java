package com.deeeelete.system.entity.query;

import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclPathDTO;
import com.deeeelete.system.entity.AclPath;
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
public class AclPathQuery extends QueryParam {
    /**
    * 原类
    */
    private AclPathDTO param;

    private QueryWrapper<AclPath> query;
    private UpdateWrapper<AclPath> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam


    // 路径名
    private String acpaNameIsLike; 





    // 备注
    private String acpaRemarkIsLike; 

    // 是否是树形态
    private String isTree = EnableEnum.STATUS_PROCESS.getCode();






    public QueryWrapper<AclPath> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclPathDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcpaId())){
            query.eq("acpa_id",getParam().getAcpaId());
            update.eq("acpa_id",getParam().getAcpaId());
        }

        // 路径名
        if(StringUtil.isNotEmpty(getParam().getAcpaName())){
            query.eq("acpa_name",getParam().getAcpaName());
            update.eq("acpa_name",getParam().getAcpaName());
        }
        if(StringUtil.isNotEmpty(getAcpaNameIsLike())){
            query.like("acpa_name",getAcpaNameIsLike());
            update.like("acpa_name",getAcpaNameIsLike());
        }

        // 上级id
        if(StringUtil.isNotEmpty(getParam().getAcpaPid())){
            query.eq("acpa_pid",getParam().getAcpaPid());
            update.eq("acpa_pid",getParam().getAcpaPid());
        }

        // 类型 0分类 1路径
        if(StringUtil.isNotEmpty(getParam().getAcpaType())){
            query.eq("acpa_type",getParam().getAcpaType());
            update.eq("acpa_type",getParam().getAcpaType());
        }

        // 备注
        if(StringUtil.isNotEmpty(getParam().getAcpaRemark())){
            query.eq("acpa_remark",getParam().getAcpaRemark());
            update.eq("acpa_remark",getParam().getAcpaRemark());
        }
        if(StringUtil.isNotEmpty(getAcpaRemarkIsLike())){
            query.like("acpa_remark",getAcpaRemarkIsLike());
            update.like("acpa_remark",getAcpaRemarkIsLike());
        }

        // 逻辑删除
        if(StringUtil.isNotEmpty(getParam().getIsDeleted())){
            query.eq("is_deleted",getParam().getIsDeleted());
            update.eq("is_deleted",getParam().getIsDeleted());
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
    public QueryWrapper<AclPath> getQueryP() {
        return query;
    }

}