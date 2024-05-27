package com.deeeelete.socket.entity.msg.req;

import com.deeeelete.socket.annotation.ReqMsgType;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;


/**
 * 普通消息
 */
@ReqMsgType(type = ReqMsgTypeEnum.TYPE_Message_log)
public class ReqMessageLogMsg extends ReqMsg {
    /**
     * 案件id
     */
    private Long caseId;
    /**
     * 最大的消息id
     */
    private Long ltMessageId;

    private Integer page = 1;
    private Integer limit = 10;

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getLtMessageId() {
        return ltMessageId;
    }

    public void setLtMessageId(Long ltMessageId) {
        this.ltMessageId = ltMessageId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
