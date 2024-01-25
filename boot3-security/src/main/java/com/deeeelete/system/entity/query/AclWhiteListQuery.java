package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclWhiteListDTO;
import com.deeeelete.system.entity.AclWhiteList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 白名单 查询类
    * </p>
*
* @author bin.xie
* @since 2024-01-25
*/
@Data
public class AclWhiteListQuery extends QueryParam {
    /**
    * 原类
    */
    private AclWhiteListDTO param;

    private QueryWrapper<AclWhiteList> query;
    private UpdateWrapper<AclWhiteList> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam


    // 接口地址
    private String acwlPathIsLike; 



    public QueryWrapper<AclWhiteList> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclWhiteListDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcwlId())){
            query.eq("acwl_id",getParam().getAcwlId());
            update.eq("acwl_id",getParam().getAcwlId());
        }

        // 接口地址
        if(StringUtil.isNotEmpty(getParam().getAcwlPath())){
            query.eq("acwl_path",getParam().getAcwlPath());
            update.eq("acwl_path",getParam().getAcwlPath());
        }
        if(StringUtil.isNotEmpty(getAcwlPathIsLike())){
            query.like("acwl_path",getAcwlPathIsLike());
            update.like("acwl_path",getAcwlPathIsLike());
        }

        // 强制锁(1不允许删除 0允许)
        if(StringUtil.isNotEmpty(getParam().getAcwlLock())){
            query.eq("acwl_lock",getParam().getAcwlLock());
            update.eq("acwl_lock",getParam().getAcwlLock());
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
    public QueryWrapper<AclWhiteList> getQueryP() {
        return query;
    }

}