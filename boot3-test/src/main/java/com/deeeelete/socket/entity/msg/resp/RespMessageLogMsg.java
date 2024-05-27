package com.deeeelete.socket.entity.msg.resp;

import com.deeeelete.socket.annotation.RespMsgType;
import com.deeeelete.socket.enums.RespMsgTypeEnum;


/**
 * 普通消息
 */
@RespMsgType(type = RespMsgTypeEnum.TYPE_message_log)
public class RespMessageLogMsg extends RespResultMsg<String> {
    private Long totalSize;

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
}
