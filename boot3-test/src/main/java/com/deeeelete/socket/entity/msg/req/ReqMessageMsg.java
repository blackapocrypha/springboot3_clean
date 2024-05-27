package com.deeeelete.socket.entity.msg.req;

import com.deeeelete.socket.annotation.ReqMsgType;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;


/**
 * 普通消息
 */
@ReqMsgType(type = ReqMsgTypeEnum.TYPE_Message)
public class ReqMessageMsg extends ReqMsg {
    /**
     * 案件id
     */
    private Long caseId;
    /**
     * 发送文件地址
     */
    private String fileUrl;
    /**
     * 发送文件类型
     */
    private Integer fileType;
    /**
     * 发送人部门id
     */
    private Long orgId;
    /**
     * 发送人部门名称
     */
    private String orgName;

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public ReqMessageMsg() {
    }
}
