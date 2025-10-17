package com.hit.telemetry_parser.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hit.telemetry_parser.domain.dto.MessageDTO;
import com.hit.telemetry_parser.entity.PacketEntity;
import com.hit.telemetry_parser.service.ITelemetryProcessService;
import io.netty.buffer.ByteBuf;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket服务
 *
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 */
@ServerEndpoint("/")
@Component
public class TelemetryServer {

    private static final Logger log = LoggerFactory.getLogger(TelemetryServer.class);

    private static ITelemetryProcessService processService;

    @Autowired
    public void setProcessService(ITelemetryProcessService processService) {
        TelemetryServer.processService = processService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen() {
        log.info("遥测转发已连接!");

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.warn("已断开");

    }

    /**
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("接收到来自服务端的消息:{}", message);
        try {
            processService.parseFrame(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


}

