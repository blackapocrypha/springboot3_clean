package com.deeeelete.system.entity.query;

import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclMenuDTO;
import com.deeeelete.system.entity.AclMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 菜单 查询类
    * </p>
*
* @author bin.xie
* @since 2023-06-24
*/
@Data
public class AclMenuQuery extends QueryParam {
    /**
    * 原类
    */
    private AclMenuDTO param;

    private QueryWrapper<AclMenu> query;
    private UpdateWrapper<AclMenu> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam


    // 菜单名称
    private String acmeNameIsLike; 



    // 代码
    private String acmeCodeIsLike; 





    // 是否可选Y是N否
    private String acmeSelectIsLike; 

    // 是否树
    private String isTree = EnableEnum.STATUS_PROCESS.getCode();






    public QueryWrapper<AclMenu> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclMenuDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getAcmeId())){
            query.eq("acme_id",getParam().getAcmeId());
            update.eq("acme_id",getParam().getAcmeId());
        }

        // 菜单名称
        if(StringUtil.isNotEmpty(getParam().getAcmeName())){
            query.eq("acme_name",getParam().getAcmeName());
            update.eq("acme_name",getParam().getAcmeName());
        }
        if(StringUtil.isNotEmpty(getAcmeNameIsLike())){
            query.like("acme_name",getAcmeNameIsLike());
            update.like("acme_name",getAcmeNameIsLike());
        }

        // 父级，0为顶级
        if(StringUtil.isNotEmpty(getParam().getAcmePid())){
            query.eq("acme_pid",getParam().getAcmePid());
            update.eq("acme_pid",getParam().getAcmePid());
        }

        // 代码
        if(StringUtil.isNotEmpty(getParam().getAcmeCode())){
            query.eq("acme_code",getParam().getAcmeCode());
            update.eq("acme_code",getParam().getAcmeCode());
        }
        if(StringUtil.isNotEmpty(getAcmeCodeIsLike())){
            query.like("acme_code",getAcmeCodeIsLike());
            update.like("acme_code",getAcmeCodeIsLike());
        }

        // 类型 1菜单 2按钮
        if(StringUtil.isNotEmpty(getParam().getAcmeType())){
            query.eq("acme_type",getParam().getAcmeType());
            update.eq("acme_type",getParam().getAcmeType());
        }

        // 排序
        if(StringUtil.isNotEmpty(getParam().getAcmeSort())){
            query.eq("acme_sort",getParam().getAcmeSort());
            update.eq("acme_sort",getParam().getAcmeSort());
        }

        // 是否可选Y是N否
        if(StringUtil.isNotEmpty(getParam().getAcmeSelect())){
            query.eq("acme_select",getParam().getAcmeSelect());
            update.eq("acme_select",getParam().getAcmeSelect());
        }
        if(StringUtil.isNotEmpty(getAcmeSelectIsLike())){
            query.like("acme_select",getAcmeSelectIsLike());
            update.like("acme_select",getAcmeSelectIsLike());
        }

        // 状态
        if(StringUtil.isNotEmpty(getParam().getAcmeStatus())){
            query.eq("acme_status",getParam().getAcmeStatus());
            update.eq("acme_status",getParam().getAcmeStatus());
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
    public QueryWrapper<AclMenu> getQueryP() {
        return query;
    }

}