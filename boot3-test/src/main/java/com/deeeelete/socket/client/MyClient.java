package com.deeeelete.socket.client;

import com.alibaba.fastjson2.JSON;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.WebSocket;
import com.ejlchina.okhttps.WebSocket.Message;
import com.deeeelete.socket.entity.msg.req.ReqHeartMsg;
import com.deeeelete.socket.enums.ReqMsgTypeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试用socket连接客户端
 */
@Slf4j
public class MyClient {


    public static void main(String[] args) {

        HTTP http = HTTP.builder().baseUrl("http://localhost:8090").build();

        // socket链接的接口地址，具体在socket.server.MyWebSocket下
        WebSocket wss = http.webSocket("/socket/7")
                .setOnMessage((WebSocket ws, Message msg) -> {
                    log.info("客户端收到服务端信息：" + msg.toString());

                    // 构造心跳包
                    ReqHeartMsg reqHeartMsg = new ReqHeartMsg();
                    reqHeartMsg.setType(ReqMsgTypeEnum.TYPE_Heart.getCode());
                    try {
                        Thread.sleep(9000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);

                    }
                    log.info("发送心跳消息：" + JSON.toJSONString(reqHeartMsg));
                    ws.send(JSON.toJSONString(reqHeartMsg));
                })
                .listen();
    }

}
