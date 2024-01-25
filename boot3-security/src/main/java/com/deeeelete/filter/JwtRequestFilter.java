package com.deeeelete.filter;

import com.alibaba.fastjson2.JSON;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.JWTUtil;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.ResponseUtil;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.utils.constent.StaticStatusConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的JWT过滤器
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private RedisTemplate redisTemplate;

    private JWTSecurityUtil jwtSecurityUtil;


    public JwtRequestFilter(RedisTemplate redisTemplate, JWTSecurityUtil jwtSecurityUtil) {
        this.jwtSecurityUtil = jwtSecurityUtil;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 从 Authorization 标头中，提取令牌
     */
    private String parseJwt(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null) {
            return null;
        }

        if (StringUtils.hasText(auth) && auth.startsWith(JWTUtil.TOKEN_HEADER)) {
            return auth.substring(JWTSecurityUtil.TOKEN_HEADER.length());
        }
        return null;
    }

    /**
     * 进行权限认证
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤链
     * @throws ServletException 异常
     * @throws IOException      异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 获取请求的url，在白名单直接放过
        String res = request.getRequestURI();
        if (checkAccrosURL(res)) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            String jwt = parseJwt(request);

            // 如果是永久的测试Key也放过,具体key在yml的 myConfig.token中配置
            String rootTestToken = (String) redisTemplate.opsForValue().get("foreverToken");
            if (!"N".equals(rootTestToken)) {
                if (jwt != null && rootTestToken.equals(jwt)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            if (jwt != null && JWTUtil.Check(jwt).isSuccess()) {
                // 获取用户拥有的权限
                List<String> codes = JWTSecurityUtil.getUser(jwt).getAuth();
                // 获取接口对应权限字符
                String pathPower = (String) redisTemplate.opsForValue().get(res);
                boolean flag = false;

                // 如果接口无权限默认全放过，否则才进行详细匹配
                if (StringUtil.isNotEmpty(pathPower)) {
                    // 解析权限，如果是空数组说明没有接口没有权限对应，直接放过
                    List<String> powers = JSON.parseArray(pathPower, String.class);
                    if (powers.size() > 0) {
                        flag = true;
                        for (String power : powers) {
                            // 接口所规定的众多权限中凡是有一个就可以放行
                            if (codes.contains(power)) {
                                flag = false;
                                break;
                            }
                        }
                    }
                }

                if (flag) {
                    ResponseUtil.buildJsonReturn(response, new JsonResult().buildFalse("权限认证失败，无接口权限"));
                    return;
                }

            } else {
                ResponseUtil.buildJsonReturn(response, new JsonResult().buildFalse("权限认证失败"));
                return;
            }
        } catch (Exception e) {
            //log.error("无法设置用户认证:{}", e);
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 白名单
     *
     * @param url 地址
     * @return boolean
     */
    private boolean checkAccrosURL(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }

        // 白名单路径放过，具体载入细节在AclPathServiceImpl的initPath方法中
        if (redisTemplate.opsForSet().isMember(StaticStatusConfig.whiteKey,url)) {
            return true;
        }

        return false;
    }
}


