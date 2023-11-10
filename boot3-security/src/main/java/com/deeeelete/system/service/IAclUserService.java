package com.deeeelete.system.service;

import com.deeeelete.system.entity.AclUser;
import com.deeeelete.system.entity.dto.AclUserDTO;
import com.deeeelete.system.entity.query.AclUserQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deeeelete.utils.JsonResult;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户 服务类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public interface IAclUserService extends IService<AclUser> {

     /**
     * 分页查询数据
     *
     * @return JsonResult
     */
     JsonResult selectAll(AclUserQuery query);


    /**
      * 根据主键删除用户
      *
      * @param id 主键
      * @return JsonResult
      */
      JsonResult deleteByKey(Long id);

      /**
      * 根据主键修改用户
      *
      * @param record 修改信息
      * @return JsonResult
      */
      JsonResult updateByKey(AclUser record);

      /**
      * 插入用户
      *
      * @param record 插入信息
      * @return JsonResult
      */
      JsonResult add(AclUser record);

    /**
     * 用户登录
     * @param user 登录
     * @return JsonResult
     */
    JsonResult userLogin(AclUserDTO user);


    /**
     * 批量删除
     * @param ids 多个id
     * @return JsonResult
     */
    JsonResult deleteBatch(String ids);

    /**
     * 导入用户
     * @param file 文件
     * @return
     */
    JsonResult excelImport(MultipartFile file);

 }
