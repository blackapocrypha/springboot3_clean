package com.deeeelete.socket.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("applicationContextHelper")
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    public ApplicationContextHolder() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextHolder.context = context;
    }

    public static Object getBeanByName(String beanName) {
        return beanName != null && context != null ? context.getBean(beanName) : null;
    }

    public static Object getBeanByType(Class clazz) {
        return clazz != null && context != null ? context.getBean(clazz) : null;
    }

    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }
}