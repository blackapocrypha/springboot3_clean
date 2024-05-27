package com.deeeelete.socket.server;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.deeeelete.socket.annotation.MsgHandleParameterType;
import com.deeeelete.socket.annotation.MsgHandleType;
import com.deeeelete.socket.entity.msg.resp.RespMsg;
import com.deeeelete.socket.entity.session.SessionManager;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;
import com.deeeelete.socket.handle.MyWebSocketHandle;
import com.deeeelete.socket.schedule.SendProductFinish;
import com.deeeelete.socket.util.ApplicationContextHolder;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

/**
 * Socket服务器接口地址
 */
@ServerEndpoint(value = "/socket/{openId}")
@Component
public class MyWebSocket {

    private MyWebSocketHandle myWebSocketHandle;

    /**
     * 建立连接成功调用
     * @param session 会话
     * @param openId  Socket唯一链接标识
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openId") String openId) {
        if (this.myWebSocketHandle == null) {
            this.myWebSocketHandle = (MyWebSocketHandle) ApplicationContextHolder.getBeanByType(MyWebSocketHandle.class);
        }
        SessionManager.addOnlineCount();
        Method[] methods = myWebSocketHandle.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MsgHandleType.class)) {
                MsgHandleType msgHandleType = method.getAnnotation(MsgHandleType.class);
                if (msgHandleType.reqType().getCode().equals(ReqMsgTypeEnum.TYPE_LINK.getCode())) {
                    try {
                        SessionManager.removeSession(openId);
                        Object result = method.invoke(myWebSocketHandle, session, openId);
                        if (result != null) {
                            RespMsg respMsg = (RespMsg) result;
                            SessionManager.sendMessage(session, respMsg.buildMsgStr());
                            if (respMsg.isClose()) {
                                try {
                                    session.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, respMsg.buildMsgStr()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 设置时间
        if (!SendProductFinish.cellList.contains(openId)) {
            SendProductFinish.cellList.add(openId);
        }
        SessionManager.sessionKeepHeartPoll.put(openId, new Date(System.currentTimeMillis()));
    }

    /**
     * 收到客户端信息
     * @param session 会话
     * @param message 消息
     * @param openId Socket唯一链接标识
     * @throws IOException
     * @throws InterruptedException
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam(value = "openId") String openId) throws IOException, InterruptedException {
        // 首先解析message
        if (null == message) {
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (jsonObject == null) {
            return;
        }
        if (null == jsonObject.get("type")) {
            return;
        }
        Method[] methods = myWebSocketHandle.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MsgHandleType.class)) {
                MsgHandleType msgHandleType = method.getAnnotation(MsgHandleType.class);
                if (msgHandleType.reqType().getCode().equals(jsonObject.getInteger("type"))) {
                    try {
                        Parameter[] parameters = method.getParameters();
                        Object[] objects = new Object[parameters.length];
                        int i = 0;
                        for (Parameter parameter : parameters) {
                            if (parameter.isAnnotationPresent(MsgHandleParameterType.class)) {
                                MsgHandleParameterType msgHandleParameterType = parameter.getAnnotation(MsgHandleParameterType.class);
                                if (msgHandleParameterType != null) {
                                    switch (msgHandleParameterType.paramType()) {
                                        case openId:
                                            objects[i++] = openId;
                                            break;
                                        case message:
                                            objects[i++] = JSON.parseObject(message, msgHandleParameterType.msgType().getClazz());
                                            break;
                                        case session:
                                            objects[i++] = session;
                                            break;
                                    }
                                }
                            }
                        }
                        Object result = method.invoke(myWebSocketHandle, objects);
                        if (result != null) {
                            RespMsg respMsg = (RespMsg) result;
                            try {
                                respMsg.setReqType(jsonObject.getInteger("type"));
                                respMsg.setOrderId(jsonObject.get("orderId").toString());
                            } catch (Exception e) {
                            }
                            SessionManager.sendMessage(session, respMsg.buildMsgStr());
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 关闭连接时调用
     * @param openId Socket唯一链接标识
     */
    @OnClose
    public void onClose(@PathParam(value = "openId") String openId) {
        SessionManager.removeSession(openId);
    }


}
