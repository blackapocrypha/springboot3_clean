package com.deeeelete.socket.entity.session;


import jakarta.websocket.Session;

/**
 * 普通用户Session
 */
public class MySession {

    private Long chatRecordId;

    private String openid;
    private Session mySession;

    public Long getChatRecordId() {
        return chatRecordId;
    }

    public void setChatRecordId(Long chatRecordId) {
        this.chatRecordId = chatRecordId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Session getMySession() {
        return mySession;
    }

    public void setMySession(Session mySession) {
        this.mySession = mySession;
    }

    public MySession() {
    }

    public MySession(Long chatRecordId, String openid, Session mySession) {
        this.chatRecordId = chatRecordId;
        this.openid = openid;
        this.mySession = mySession;
    }
}
