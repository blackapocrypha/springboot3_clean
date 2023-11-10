package com.deeeelete.system.controller;


import com.deeeelete.pojo.JWTUser;
import com.deeeelete.system.entity.dto.AclUserDTO;
import com.deeeelete.system.service.IAclUserService;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.JsonResult;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/acl")
public class AclLoginController {

    @Autowired
    private IAclUserService service;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 登录
     * @param aclUser 登录用户
     * @return JsonResult
     */
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(AclUserDTO aclUser){
        return service.userLogin(aclUser);
    }

    /**
     * 登出
     * @param token 令牌
     * @return JsonResult
     */
    @PostMapping("/logOut")
    @ResponseBody
    public JsonResult logOut(@RequestParam("token") String token){
        JsonResult jsonResult = new JsonResult();
        try {
            JWTUser user = JWTSecurityUtil.getUser(token);
            redisTemplate.delete("user::" + user.getId());
        }catch (ExpiredJwtException e){
            jsonResult.buildTrue();
            return jsonResult;
        }

        jsonResult.buildTrue();
        return jsonResult;
    }




}
