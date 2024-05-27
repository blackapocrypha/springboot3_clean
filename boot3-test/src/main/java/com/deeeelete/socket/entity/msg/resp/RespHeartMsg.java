package com.deeeelete.socket.entity.msg.resp;

import com.deeeelete.socket.annotation.RespMsgType;
import com.deeeelete.socket.enums.RespMsgTypeEnum;


/**
 * 普通消息
 */
@RespMsgType(type = RespMsgTypeEnum.TYPE_Heart)
public class RespHeartMsg extends RespResultMsg<String> {


}
