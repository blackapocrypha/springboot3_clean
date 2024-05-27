package com.deeeelete.socket.entity.msg.resp;

import com.alibaba.fastjson2.JSON;
import com.deeeelete.socket.annotation.RespMsgType;

import java.util.List;

public class RespMsg<T> {
    private List<T> data;
    private Object dataObject;
    private Integer type;
    private String remark;
    private String senderName;
    // 群组id
    private Long caseId;

    // 消息流水号
    private String orderId;
    private Integer reqType;
    // 是否关闭连接，用于连接登录的返回错误时，自动关闭连接，默认为false
    private boolean close = false;

    public String buildMsgStr(){
        if(this.getClass().isAnnotationPresent(RespMsgType.class)){
            RespMsgType respMsgType = this.getClass().getAnnotation(RespMsgType.class);
            if(respMsgType != null){
                setType(respMsgType.type().getCode());
            }
        }
        return JSON.toJSONString(this);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Object getDataObject() {
        return dataObject;
    }

    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getReqType() {
        return reqType;
    }

    public void setReqType(Integer reqType) {
        this.reqType = reqType;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}
