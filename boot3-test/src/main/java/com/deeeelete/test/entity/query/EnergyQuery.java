package com.deeeelete.test.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.test.entity.Energy;
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
* @since 2024-01-26
*/
@Data
public class EnergyQuery extends QueryParam {


    private QueryWrapper<Energy> query;

    private UpdateWrapper<Energy> update;

    // likeParam
    // 主键
    private Integer id;

    // 名称
    private String name;

    // 年龄
    private Integer age;


    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    public QueryWrapper<Energy> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();





        // 主键
        if(StringUtil.isNotEmpty(getId())){
        query.eq("id",getId());
        update.eq("id",getId());
        }


        if(StringUtil.isNotEmpty(getName())){
            query.like("name",getName());
            update.like("name",getName());
        }


        // 年龄
        if(StringUtil.isNotEmpty(getAge())){
        query.eq("age",getAge());
        update.eq("age",getAge());
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
    public QueryWrapper<Energy> getQueryP() {
        return query;
    }

}