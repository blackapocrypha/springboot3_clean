package com.deeeelete.system.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class HttpContextUtil {

    /*
     * RequestContextHolder ： 持有上下文的Request容器
     * */

    // 获取Request对象
    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = null;
        try {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return request;
        }

        return request;
    }

}