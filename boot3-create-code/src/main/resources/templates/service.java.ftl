package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Entity}.query.${entity}Query;
import ${superServiceClassPackage};
import com.deeeelete.utils.JsonResult;


/**
 * ${table.comment!} 服务类
 *
 * @author ${author}
 * @since ${date}
*/
<#if kotlin>
 interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(${entity}Query query);


    /**
      * 根据主键删除${table.comment!}
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改${table.comment!}
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(${entity} record);

      /**
      * 插入${table.comment!}
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(${entity} record);


 }
</#if>
