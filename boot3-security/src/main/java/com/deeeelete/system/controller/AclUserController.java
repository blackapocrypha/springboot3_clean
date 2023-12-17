package com.deeeelete.system.controller;

import com.deeeelete.annotation.SysLog;
import com.deeeelete.pojo.JWTUser;
import com.deeeelete.system.entity.AclUser;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.entity.query.AclUserQuery;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.deeeelete.system.service.IAclUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
*
* 用户 前端控制器
*
* @author bin.xie
* @since 2023-05-30
*/
@RestController
@Slf4j
@RequestMapping("/system/acl-user")
public class AclUserController {
    @Resource
    private IAclUserService service;

    /**
     * 用户key
     */
    private static String redisStringKey = "user::";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
    * 分页查询用户
    *
    * @return JsonResult
    */
    @PostMapping(value = "/selectByExample")
    public JsonResult selectByExample(@ModelAttribute AclUserQuery query) {
        return service.selectAll(query);
    }

    /**
    * 插入用户
    *
    * @param record 插入信息
    * @return JsonResult
    */
    @PostMapping(value = "/insert")
    @SysLog(client = ClientEnum.ADMIN,act = "插入用户")
    public JsonResult insert(@ModelAttribute AclUser record) {
        return service.add(record);
    }

    /**
     * 导入用户
     * @param file 文件
     * @return JsonResult
     */
    @PostMapping(value = "/excelImport")
    public JsonResult excelImport(@ModelAttribute MultipartFile file){ return service.excelImport(file);}

    /**
    * 根据主键修改用户
    *
    * @param record 修改信息
    * @return JsonResult
    */
    @PostMapping(value = "/update")
    @SysLog(client = ClientEnum.ADMIN,act = "更新用户")
    public JsonResult update(@ModelAttribute AclUser record) {
        return service.updateByKey(record);
    }

    /**
    * 根据主键删除用户
    *
    * @param id 主键id
    * @return JsonResult
    */
    @PostMapping(value = "/delete")
    @SysLog(client = ClientEnum.ADMIN,act = "删除用户")
    public JsonResult delete(@RequestParam(value = "id")  Long id) {
        return service.deleteByKey(id);
    }


    /**
     * 批量删除
     * @param ids 多个id用逗号隔开
     * @return JsonResult
     */
    @PostMapping(value = "/deleteBatch")
    @SysLog(client = ClientEnum.ADMIN,act = "批量删除用户")
    public JsonResult deleteBatch(@RequestParam(value = "ids")  String ids) {
        return service.deleteBatch(ids);
    }

    /**
     * 获取用户信息
     * @param request 请求参数
     * @return JsonResult
     */
    @PostMapping(value = "/getUserInfo")
    public JsonResult getUserInfo(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        JWTUser loginUser = JWTSecurityUtil.getLoginUser(request);
        if (StringUtil.isEmpty(loginUser)) {
            jsonResult.buildFalse("未登录");
            return jsonResult;
        }
        String currentUserKey = redisStringKey + loginUser.getId();
        Object redisUserToken = redisTemplate.opsForValue().get(currentUserKey);
        if(StringUtil.isEmpty(redisUserToken)){
            redisTemplate.delete(currentUserKey);
            jsonResult.buildFalse("登录用户已过期");
            return jsonResult;
        }
        jsonResult.buildTrue();
        jsonResult.setData(loginUser);
        return jsonResult;
    }


}
