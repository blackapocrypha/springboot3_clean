package com.deeeelete.socket.entity.msg.req;

import com.deeeelete.socket.annotation.ReqMsgType;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;


/**
 * 普通消息
 */
@ReqMsgType(type = ReqMsgTypeEnum.TYPE_Heart)
public class ReqHeartMsg extends ReqMsg {

}
