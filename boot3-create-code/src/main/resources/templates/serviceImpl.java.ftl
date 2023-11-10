package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Entity}.query.${entity}Query;
import ${package.Entity}.dto.${entity}DTO;
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import com.deeeelete.utils.EntityUtil;
import org.springframework.stereotype.Service;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${""}${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${""}${table.mapperName}, ${entity}> implements ${table.serviceName} {

    /**
    * 分页查询${table.comment!}
    *
    * @return JsonResult
    */
    @Override
    public JsonResult selectAll(${entity}Query query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<${entity}> page = query.buildPage(this);
        List<${entity}DTO> records = EntityUtil.parentListToChildList(page.getRecords(), ${entity}DTO.class);
        if(ListUtil.isNotEmpty(records)){
            jsonResult.buildTrue();
            jsonResult.setData(records);
            jsonResult.setTotalsize(page.getTotal());
        }else {
            jsonResult.buildFalse("无数据");
        }
        return jsonResult;
    }

    /**
     * 根据主键删除${table.comment!}
     *
     * @param id 主键
     * @return JsonResult
    */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        if(removeById(id)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改${table.comment!}
     *
     * @param record 修改信息
     * @return JsonResult
    */
    @Override
    public JsonResult updateByKey(${entity} record) {
        JsonResult jsonResult = new JsonResult();
        if(updateById(record)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入${table.comment!}
     *
     * @param record 插入信息
     * @return JsonResult
    */
    @Override
    public JsonResult add(${entity} record) {
        JsonResult jsonResult = new JsonResult();
        if(save(record)){
            jsonResult.buildTrue();
        }else{
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }


}
</#if>
