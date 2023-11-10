package com.deeeelete.system.util;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.pojo.JWTUser;
import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.AclUserRole;
import com.deeeelete.system.service.IAclPathService;
import com.deeeelete.system.service.IAclRoleService;
import com.deeeelete.system.service.IAclUserRoleService;
import com.deeeelete.utils.JWTUtil;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ListUtil;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.utils.constent.StaticStatusConfig;
import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * jwt权限判别工具
 */
@Component
public class JWTSecurityUtil {

    /**
     * 签名
     */
    public static String signature = "530201197901273093";

    private static long time = 109 * 1000 * 60 * 60 * 24;//24小时

    /**
     * token前缀
     */
    public static final String TOKEN_HEADER = "Wareer ";

    @Resource
    private IAclRoleService aclRoleService;

    @Resource
    private IAclUserRoleService aclUserRoleService;

    @Resource
    private IAclPathService pathService;

    /**
     * jwt加密token json
     *
     * @param user 用户
     * @return String
     */
    public String jwtEncode(JWTUser user) {
        JwtBuilder jwt = Jwts.builder();
        setCurrentUserRoleCode(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user);
        claims.put("iat", new Date());
        claims.put("exp", new Date(System.currentTimeMillis() + time));
        String jwtToken = jwt
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")//加密算法
                .setClaims(claims)
                //payload
                .setSubject(signature)//主题
                .setExpiration(new Date((System.currentTimeMillis() + time))) //有效时间
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
        return TOKEN_HEADER + jwtToken;
    }


    /**
     * jwt解密
     *
     * @param jwtToken 加密的token
     * @return Claims
     */
    public static Claims jwtDecode(String jwtToken) {
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(signature).parseClaimsJws(jwtToken);
        return claimsJws.getBody();
    }


    /**
     * 基本检查Token的合法性
     *
     * @param jwtToken  token数据
     * @return JsonResult
     */
    public static JsonResult Check(String jwtToken) {

        JsonResult result = new JsonResult();

        // 1 判空
        if (jwtToken == null) {
            return result.buildFalse("用户未登录！");
        }

        Claims claims = null;
        try {
            claims = jwtDecode(jwtToken);
        } catch (ExpiredJwtException e) {
            //有可能出现JWT过期错误
            return result.buildFalse(e.getMessage());
        } catch (SignatureException e) {
            return result.buildFalse("token格式错误");
        }


        // 2 检查是否是合法token
        if (!claims.getSubject().equals(signature)) {
            result.setStatus(StaticStatusConfig.ILLEGAL_TOKEN);
            result.setMessage("非法token");
            return result;
        }

        // 2 检查token是否超时
        if (claims.getExpiration().compareTo(new Date(System.currentTimeMillis())) != 1) {
            result.setStatus(StaticStatusConfig.TOKEN_EXPIRED);
            result.setMessage("token已超时");
            return result;
        }

        result.setStatus(JsonResult.STATUS_SUCCESS);
        return result;
    }


    /**
     * 根据token获取用户信息
     *
     * @param jwtToken token内容
     * @return JWTUser
     */
    public static JWTUser getUser(String jwtToken) {
        if (StringUtil.isEmpty(jwtToken)) {
            return null;
        }
        if (jwtToken.startsWith(TOKEN_HEADER)) {
            jwtToken = jwtToken.substring(JWTSecurityUtil.TOKEN_HEADER.length());
        }
        Claims claims = jwtDecode(jwtToken);
        return JSON.parseObject(JSON.toJSONString(claims.get("user")), JWTUser.class);
    }


    /**
     * 根据获取用户当前拥有的全部角色
     *
     * @param user 用户
     */
    public void setCurrentUserRoleCode(JWTUser user) {
        List<String> auth = new ArrayList<>();
        List<AclUserRole> aclUserRoles = aclUserRoleService.list(new LambdaQueryWrapper<AclUserRole>().eq(AclUserRole::getAcurUserId, user.getId()));
        if (ListUtil.isNotEmpty(aclUserRoles)) {
            List<Long> roleIds = ListUtil.transformationByListToList(aclUserRoles, AclUserRole::getAcurRoleId);
            List<AclRole> aclRoles = aclRoleService.list(new LambdaQueryWrapper<AclRole>().in(AclRole::getRoleId, roleIds));
            auth = aclRoles.stream().map(m -> m.getRoleCode()).collect(Collectors.toList());
        }
        user.setAuth(auth);
    }

    /**
     * 获取请求登录用户信息
     *
     * @param request 请求
     * @return JWTUser
     */
    public static JWTUser getLoginUser(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (StringUtil.isEmpty(auth)) {
            return null;
        }

        if (StringUtils.hasText(auth) && auth.startsWith(JWTUtil.TOKEN_HEADER)) {
            auth = auth.substring(JWTSecurityUtil.TOKEN_HEADER.length());
        }
        return getUser(auth);
    }


    /**
     * 在redis中初始化权限
     */
    public void initPath() {
        pathService.initPath();
    }

}
