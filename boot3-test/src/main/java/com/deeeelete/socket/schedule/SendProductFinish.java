package com.deeeelete.socket.schedule;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.deeeelete.socket.entity.msg.resp.RespMessageMsg;
import com.deeeelete.socket.entity.session.MySession;
import com.deeeelete.socket.entity.session.SessionManager;
import com.deeeelete.socket.enums.RespMsgTypeEnum;
import com.deeeelete.utils.ListUtil;
import com.deeeelete.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 定时任务
 */
@Component
public class SendProductFinish {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // 存储openId
    public static CopyOnWriteArrayList<String> cellList = new CopyOnWriteArrayList();

    /**
     * 每小时整点执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void executeTask() {
        // type = 1的消息，提醒前端刷新
        RespMessageMsg respMessageMsg = new RespMessageMsg();
        respMessageMsg.setType(RespMsgTypeEnum.TYPE_message.getCode());
        respMessageMsg.setData(null);
        respMessageMsg.setClose(false);

        // 实际上应该对全部消息进行发送
        CopyOnWriteArrayList<String> openIds = cellList;
        for (String openId : openIds) {
            MySession mySession = SessionManager.sessionPools.get(openId);
            if (StringUtil.isNotEmpty(mySession)) {
                SessionManager.sendMessage(mySession.getMySession(), JSON.toJSONString(respMessageMsg));
            }
        }
    }


    /**
     * 每30秒钟进行一次消息发送
     */
    @Scheduled(fixedRate = 30000)
    public void output10() {

        RespMessageMsg respMessageMsg = new RespMessageMsg();
        respMessageMsg.setType(RespMsgTypeEnum.TYPE_RESULT.getCode());
        respMessageMsg.setClose(false);
        // 实际上应该对全部消息进行发送
        CopyOnWriteArrayList<String> openIds = cellList;
        for (String openId : openIds) {
            MySession mySession = SessionManager.sessionPools.get(openId);
            if (StringUtil.isNotEmpty(mySession)) {
                SessionManager.sendMessage(mySession.getMySession(), JSON.toJSONString(respMessageMsg));
            }
        }
    }


    /**
     * 每2秒对Session进行判断，一分钟无心跳则进行清理，【不要删除】
     */
    @Scheduled(fixedRate = 2000)
    public void cleanSessionIfNoHeart() {
        // 取出所有id，测试使用
        List<String> idList = cellList;
        if (ListUtil.isEmpty(idList)) {
            List<String> collect = new ArrayList<>();
            if (ListUtil.isNotEmpty(collect)) {
                idList.addAll(collect);
            }
            cellList.addAll(collect);
            // 这是客户端测试用的编号7，正式使用需要删除
            if (!cellList.contains("7")) {
                cellList.add("7");
            }
            idList = collect;
        }

        // 对超时的session进行清理
        for (String id : idList) {
            Date lastDate = SessionManager.sessionKeepHeartPoll.get(id + "");
            // 如果记录过最后时间，判断超时并进行清除
            if (StringUtil.isNotEmpty(lastDate)) {
                // 六十分钟没有心跳就进行清理
                if (DateUtil.between(lastDate, new Date(), DateUnit.MINUTE) > 1) {
                    log.info("已经清除 " + id);
                    SessionManager.sessionKeepHeartPoll.remove(id + "");
                    SessionManager.removeSession(id + "");
                }
            }
        }

    }

}
