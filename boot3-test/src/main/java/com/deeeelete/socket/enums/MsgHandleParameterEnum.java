package com.deeeelete.socket.enums;

import com.deeeelete.socket.entity.msg.req.ReqMsg;

import jakarta.websocket.Session;
/**
 * 信息处理方法的参数类型
 */
public enum MsgHandleParameterEnum {
    /**
     * 类型
     */
    session(Session.class),
    message(ReqMsg.class),
    openId,
    ;
    private Class clazz;

    public Class getClazz() {
        return clazz;
    }
    MsgHandleParameterEnum(Class clazz){
        this.clazz = clazz;
    }
    MsgHandleParameterEnum(){
        this.clazz = clazz;
    }
}
