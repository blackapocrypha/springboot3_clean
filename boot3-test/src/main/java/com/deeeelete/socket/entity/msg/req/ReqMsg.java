package com.deeeelete.socket.entity.msg.req;


public class ReqMsg {
    private String msg;
    private Integer type;

    // 消息流水号
    private String orderId;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ReqMsg() {
    }
}
