package com.deeeelete.socket.handle;

import com.deeeelete.socket.annotation.MsgHandleParameterType;
import com.deeeelete.socket.annotation.MsgHandleType;
import com.deeeelete.socket.entity.msg.req.ReqHeartMsg;
import com.deeeelete.socket.entity.msg.req.ReqMsg;
import com.deeeelete.socket.entity.msg.resp.RespHeartMsg;
import com.deeeelete.socket.entity.msg.resp.RespResultMsg;
import com.deeeelete.socket.entity.session.MySession;
import com.deeeelete.socket.entity.session.SessionManager;
import com.deeeelete.socket.enums.MsgHandleParameterEnum;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.StringUtil;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * webSocket消息处理器，对不同type类型的消息进行分别处理
 */
@Component
@Slf4j
public class MyWebSocketHandle {


    // 建立连接成功调用
    @MsgHandleType(reqType = ReqMsgTypeEnum.TYPE_LINK)
    public RespResultMsg onOpen(Session session, String openId) {
        RespResultMsg resultMsg = new RespResultMsg();

        MySession mySession = new MySession();
        mySession.setMySession(session);
        mySession.setOpenid(openId);
        SessionManager.putMySession(openId, mySession);
        log.info("socket用户" + openId + "连接成功");
        //SendProductFinish.cellList.add(openId);
        return resultMsg.buildTrue();
    }

    // 普通消息
    @MsgHandleType(reqType = ReqMsgTypeEnum.TYPE_Message)
    public RespResultMsg message(@MsgHandleParameterType(paramType = MsgHandleParameterEnum.session) Session session,
                                 @MsgHandleParameterType(msgType = ReqMsgTypeEnum.TYPE_Message, paramType = MsgHandleParameterEnum.message) ReqMsg reqMsg,
                                 @MsgHandleParameterType(paramType = MsgHandleParameterEnum.openId) String openId) throws IOException, InterruptedException {
        RespResultMsg resultMsg = new RespResultMsg();
        if (reqMsg == null) {
            return resultMsg.buildFalse();
        }
        return resultMsg.buildTrue();
    }


    // 心跳消息
    @MsgHandleType(reqType = ReqMsgTypeEnum.TYPE_Heart)
    public RespResultMsg message(@MsgHandleParameterType(paramType = MsgHandleParameterEnum.session) Session session,
                                 @MsgHandleParameterType(msgType = ReqMsgTypeEnum.TYPE_Heart, paramType = MsgHandleParameterEnum.message) ReqHeartMsg reqMsg,
                                 @MsgHandleParameterType(paramType = MsgHandleParameterEnum.openId) String openId) throws IOException, InterruptedException {
        RespHeartMsg resultMsg = new RespHeartMsg();
        // 实时更新对应session的时间
        SessionManager.sessionKeepHeartPoll.put(openId, new Date());
        //log.info("收到心跳消息~");
        return resultMsg.buildTrue();
    }



    private RespResultMsg verificationParam(MySession userSession, Session session, ReqMsg reqMsg, RespResultMsg resultMsg) {
        if (userSession == null) {
            return resultMsg.buildFalse();
        }
        if (userSession.getMySession() == null) {
            return resultMsg.buildFalse();
        }
        if (!userSession.getMySession().getId().equals(session.getId())) {
            return resultMsg.buildFalse();
        }
        return resultMsg.buildTrue();
    }
}
