package com.deeeelete.utils;


import com.alibaba.fastjson2.JSON;

import com.deeeelete.pojo.JWTUser;
import com.deeeelete.utils.constent.StaticStatusConfig;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {

    /**
     * 签名
     */
    public static String signature = "530201197901273093";

    private static long time = 109 * 1000 * 60 * 60 * 24;//24小时

    /**
     * token前缀
     */
    public static final String TOKEN_HEADER = "Wareer ";



    /**
     * jwt加密token json
     *
     * @param user 用户
     * @return
     */
    public static String jwtEncode(JWTUser user) {
        JwtBuilder jwt = Jwts.builder();

        String jwtToken = jwt
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")//加密算法
                //payload
                .claim("user", user)
                .claim("u1", user.toString())
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
    public static Claims jwtDecode(String jwtToken){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(signature).parseClaimsJws(jwtToken);
        return claimsJws.getBody();
    }


    /**
     * 基本检查Token的合法性
     *
     * @param jwtToken
     * @return
     */
    public static JsonResult Check(String jwtToken) {

        JsonResult result = new JsonResult();

        // 1 判空
        if(jwtToken == null){
            return result.buildFalse("用户未登录！");
        }

        Claims claims = null;
        try {
            claims = jwtDecode(jwtToken);
        } catch (ExpiredJwtException e) {
            //有可能出现JWT过期错误
            return  result.buildFalse(e.getMessage());
        } catch (SignatureException e){
            return  result.buildFalse("token格式错误");
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
     * 获取用户
     * @param jwtToken
     * @return
     */
    public static JWTUser getUser(String jwtToken) {
        Claims claims = jwtDecode(jwtToken);
        JWTUser jwtUser = JSON.parseObject(JSON.toJSONString(claims.get("user")), JWTUser.class);
        return jwtUser;
    }




}
