package com.deeeelete.socket.enums;


import com.deeeelete.utils.StringUtil;

/**
 * 发送消息类型
 */
public enum RespMsgTypeEnum {
    /**
     * 类型
     */
    TYPE_Online_notification("上线通知", 0),
    TYPE_message("普通消息", 1),
    TYPE_RESULT("通用回复", 2),
    TYPE_message_log("获取普通消息记录", 3),

    TYPE_Heart("心跳消息", 100),
    TYPE_OTHER("其他", -1),
    ;

    /**
     * 说明
     */
    private final String meaning;

    /**
     * 代码
     */
    private final Integer code;

    public String getMeaning() {
        return meaning;
    }

    public Integer getCode() {
        return code;
    }

    RespMsgTypeEnum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }
    public static RespMsgTypeEnum getType(Integer code){
        if(StringUtil.isNotEmpty(code)){
            for(RespMsgTypeEnum type: RespMsgTypeEnum.values()){
                if(type.getCode().equals(code)){
                    return type;
                }
            }
        }
        return TYPE_OTHER;
    }
}
