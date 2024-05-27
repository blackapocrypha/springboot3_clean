package com.deeeelete.socket.entity.msg.resp;

import com.deeeelete.socket.annotation.RespMsgType;
import com.deeeelete.socket.enums.RespMsgTypeEnum;


/**
 * 普通消息
 */
@RespMsgType(type = RespMsgTypeEnum.TYPE_RESULT)
public class RespResultMsg<T> extends RespMsg<T> {
    private boolean success;
    private String failReason;

    public RespResultMsg buildFalse(String failReason,boolean close) {
        buildFalse(failReason);
        setClose(close);
        return this;
    }

    public RespResultMsg buildFalse(String failReason) {
        buildFalse();
        setFailReason(failReason);
        return this;
    }

    public RespResultMsg buildFalse() {
        setSuccess(false);
        return this;
    }

    public RespResultMsg buildTrue() {
        setSuccess(true);
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
