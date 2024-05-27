package com.deeeelete.socket.annotation;

import com.deeeelete.socket.enums.RespMsgTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RespMsgType {
    RespMsgTypeEnum type() default RespMsgTypeEnum.TYPE_OTHER;
}
