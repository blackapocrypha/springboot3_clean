package com.deeeelete.socket.enums;

import com.deeeelete.socket.entity.msg.req.ReqHeartMsg;
import com.deeeelete.socket.entity.msg.req.ReqMessageLogMsg;
import com.deeeelete.socket.entity.msg.req.ReqMessageMsg;
import com.deeeelete.socket.entity.msg.req.ReqMsg;
import com.deeeelete.utils.StringUtil;

/**
 * 请求消息类型
 */
public enum ReqMsgTypeEnum {
    /**
     * 类型
     */
    TYPE_LINK("连接", 0, ReqMsg.class),
    TYPE_Message("普通消息", 1, ReqMessageMsg.class),
    TYPE_Message_log("获取普通消息记录", 2, ReqMessageLogMsg.class),
    TYPE_Heart("心跳消息", 100, ReqHeartMsg.class),
    TYPE_OTHER("其他", -1, ReqMsg.class),
    ;

    /**
     * 说明
     */
    private final String meaning;

    /**
     * 代码
     */
    private final Integer code;
    private final Class clazz;

    public String getMeaning() {
        return meaning;
    }

    public Integer getCode() {
        return code;
    }

    public Class getClazz() {
        return clazz;
    }

    ReqMsgTypeEnum(String meaning, Integer code, Class clazz) {
        this.meaning = meaning;
        this.code = code;
        this.clazz = clazz;
    }

    public static ReqMsgTypeEnum getType(Integer code) {
        if (StringUtil.isNotEmpty(code)) {
            for (ReqMsgTypeEnum type : ReqMsgTypeEnum.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return TYPE_OTHER;
    }

    public static ReqMsgTypeEnum getType(String meaning) {
        if (StringUtil.isNotEmpty(meaning)) {
            for (ReqMsgTypeEnum type : ReqMsgTypeEnum.values()) {
                if (type.getMeaning().equals(meaning)) {
                    return type;
                }
            }
        }
        return TYPE_OTHER;
    }
}
