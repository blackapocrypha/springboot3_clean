package ${package.Controller};

import ${package.Entity}.vo.${entity}VO;
import ${package.Entity}.query.${entity}Query;
import lombok.extern.slf4j.Slf4j;
import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.*;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
*
* ${table.comment!} 前端控制器
*
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@Slf4j
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>
    @Resource
    private ${table.serviceName} service;

    /**
    * 分页查询${table.comment!}
    *
    * @return JsonResult
    */
    @GetMapping(value = "/selectByExample")
    public JsonResult selectByExample(${entity}Query query) {
        return service.selectAll(query);
    }

    /**
    * 插入${table.comment!}
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    public JsonResult insert(@RequestBody ${entity}VO record) {
        return service.add(record);
    }

    /**
    * 根据主键修改${table.comment!}
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PutMapping(value = "/update")
    public JsonResult update(@RequestBody ${entity}VO record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除${table.comment!}
    *
    * @param id 主键id
    * @return JsonResult
    */
    @DeleteMapping(value = "/delete")
    public JsonResult delete(@RequestParam(value = "id") Long id) {
        return service.deleteByKey(id);
    }

}
</#if>
