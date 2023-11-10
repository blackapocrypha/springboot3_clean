package com.deeeelete.annotation;
import com.deeeelete.system.entity.enums.ClientEnum;

import java.lang.annotation.*;

/**
 * 测试日志注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    // 操作数据端
    ClientEnum client() default ClientEnum.ADMIN;

    // 操作的行为
    String act();
}
