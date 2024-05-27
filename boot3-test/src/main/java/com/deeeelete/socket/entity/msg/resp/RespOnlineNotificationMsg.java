package com.deeeelete.socket.entity.msg.resp;

import com.deeeelete.socket.annotation.RespMsgType;
import com.deeeelete.socket.enums.RespMsgTypeEnum;


/**
 * 上线提醒
 */
@RespMsgType(type = RespMsgTypeEnum.TYPE_Online_notification)
public class RespOnlineNotificationMsg extends RespMsg {

}
