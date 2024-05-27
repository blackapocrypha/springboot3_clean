package com.deeeelete.socket.annotation;

import com.deeeelete.socket.enums.MsgHandleParameterEnum;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MsgHandleParameterType {
    MsgHandleParameterEnum paramType();
    ReqMsgTypeEnum msgType() default ReqMsgTypeEnum.TYPE_OTHER;
}
