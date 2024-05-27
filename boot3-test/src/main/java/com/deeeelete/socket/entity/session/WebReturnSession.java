package com.deeeelete.socket.entity.session;



import java.util.Map;

public class WebReturnSession {
    private String openid;
    private Map<String, Object> user;
    private Object loginUser;
    private SessionType sessionType;

    public WebReturnSession(UserSession session) {
        this.openid = session.getOpenid();
        this.sessionType = SessionType.UserSession;
    }
    public enum SessionType{
        LawyerSession,LawyerTransferSession,UserSession;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public Object getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Object loginUser) {
        this.loginUser = loginUser;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public WebReturnSession() {
    }

    public WebReturnSession(String openid, Map<String, Object> user, Object loginUser, SessionType sessionType) {
        this.openid = openid;
        this.user = user;
        this.loginUser = loginUser;
        this.sessionType = sessionType;
    }
}
