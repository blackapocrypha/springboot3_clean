package ${package.Entity}.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import ${package.Entity}.dto.${entity}DTO;
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * ${table.comment!} 查询类
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Data
public class ${entity}Query extends QueryParam {

    private QueryWrapper<${entity}> query;
    private UpdateWrapper<${entity}> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

<#list table.fields as field>
    <#if field.propertyType!?contains('String')>
    // ${field.comment}
    private String ${field.propertyName}IsLike;
    <#else >
    // ${field.comment}
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>

    // likeParam
<#list table.fields as field>
        <#if field.propertyType!?contains('String')>
    // ${field.comment}
    private String ${field.propertyName}IsLike; </#if>

</#list>
    public QueryWrapper<${entity}> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new ${entity}DTO());
        }

    <#list table.fields as field>
        <#if field.propertyType!?contains('String')>
            if(StringUtil.isNotEmpty(get${field.propertyName?cap_first}IsLike())){
                query.like("${field.name}",get${field.propertyName?cap_first}IsLike());
                update.like("${field.name}",get${field.propertyName?cap_first}IsLike());
            }
        <#else >
            // ${field.comment}
            if(StringUtil.isNotEmpty(get${field.propertyName?cap_first}())){
                query.eq("${field.name}",get${field.propertyName?cap_first}());
                update.eq("${field.name}",get${field.propertyName?cap_first}());
            }
        </#if>
    </#list>

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
    public QueryWrapper<${entity}> getQueryP() {
        return query;
    }

}