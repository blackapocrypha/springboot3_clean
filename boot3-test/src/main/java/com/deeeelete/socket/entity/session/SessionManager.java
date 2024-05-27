package com.deeeelete.socket.entity.session;

import com.deeeelete.socket.entity.msg.resp.RespMsg;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    public static AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。key为用户openid
    public static ConcurrentHashMap<String, MySession> sessionPools = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, UserSession> userSessionPools = new ConcurrentHashMap<>();

    // 用户openid为key，用户信息为value
    public static ConcurrentHashMap<String, Object> sessionIdUserPools = new ConcurrentHashMap<>();

    // 用来存储心跳时间
    public static ConcurrentHashMap<String, Date> sessionKeepHeartPoll = new ConcurrentHashMap<>();


    //发送消息
    public static void sendMessage(Session session, String message) {
        if (session != null) {
            synchronized (session) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(Session session, RespMsg message) {
        sendMessage(session, message.buildMsgStr());
    }

    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }


    public static void putMySession(String openid, MySession session) {
        // 添加之前，将原连接关闭
        removeSession(openid);
        sessionPools.put(openid, session);
        //sessionIdUserPools.put(openid, session.getLoginUser());
    }

    public static void putSession(String openid, UserSession session) {
        putMySession(openid, session);
        userSessionPools.put(openid, session);
    }

    public static void removeSession(String openid) {
        try {
            if (sessionPools.containsKey(openid)) {
                try {
                    sessionPools.get(openid).getMySession().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sessionPools.remove(openid);
                userSessionPools.remove(openid);
                sessionIdUserPools.remove(openid);
                List<Long> removeCaseId = new ArrayList<>();

                subOnlineCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
