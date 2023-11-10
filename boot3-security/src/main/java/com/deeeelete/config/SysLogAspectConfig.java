package com.deeeelete.config;

import com.alibaba.fastjson2.JSON;
import com.deeeelete.annotation.SysLog;
import com.deeeelete.pojo.JWTUser;
import com.deeeelete.system.entity.SysHistoryLogs;
import com.deeeelete.system.entity.enums.ClientEnum;
import com.deeeelete.system.service.ISysHistoryLogsService;
import com.deeeelete.system.service.impl.ThreadPoolSysHandleServiceImpl;
import com.deeeelete.system.util.HttpContextUtil;
import com.deeeelete.system.util.IpUtil;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.StringUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


import java.util.Date;


/**
 * 日志切面
 */
@Aspect
@Component
public class SysLogAspectConfig {

    private static Long currentTime;

    @Resource
    private ISysHistoryLogsService sysHistoryLogsService;

    @Resource
    private ThreadPoolTaskExecutor executor;


    //切入点标明作用在所有被MyLog注解的方法上
    @Pointcut("@annotation(com.deeeelete.annotation.SysLog)")
    public void cutPoint() {
    }

    @Before("cutPoint()")
    public void Before(JoinPoint joinPoint) {
        currentTime = System.currentTimeMillis();// 当前时间
    }


    @Around("cutPoint()")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {

        SysHistoryLogs systemLog = new SysHistoryLogs();

        HttpServletRequest httpServletRequest = HttpContextUtil.getHttpServletRequest();// 获取http请求
        SysLog log = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(SysLog.class);// 获取日志对象
        ClientEnum client = log.client();//获取Client
        String act = log.act();// 获取操作行为
        Object proceed = null; // 返回值对象
        String responseData = "";
        String requestURI = httpServletRequest.getRequestURI(); //获取请求地址

        proceed = joinPoint.proceed();
        if(StringUtil.isNotEmpty(proceed)){
            responseData = JSON.toJSONString(proceed);
        }

        // 用户信息
        JWTUser loginUser = JWTSecurityUtil.getLoginUser(httpServletRequest);
        if(StringUtil.isNotEmpty(loginUser)){
            systemLog.setSlogLoginUserId(loginUser.getId());
            systemLog.setSlogRequestIp(IpUtil.getIpAddress(httpServletRequest));
            systemLog.setSlogRequestMethod(act);
            systemLog.setSlogRequestUrl(requestURI);
            systemLog.setSlogRequestData(httpServletRequest.getQueryString());
            systemLog.setSlogResponseData(responseData);
            systemLog.setSlogUserAccount(loginUser.getUserAccount());
            systemLog.setSlogClient(client.getMeaning());
            systemLog.setGmtCreate(new Date());
            executor.execute(new ThreadPoolSysHandleServiceImpl(systemLog,sysHistoryLogsService));
        }

        return proceed;
    }



}
