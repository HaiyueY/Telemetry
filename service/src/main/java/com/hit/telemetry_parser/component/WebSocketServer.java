package com.hit.telemetry_parser.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hit.telemetry_parser.common.Result;
import com.hit.telemetry_parser.domain.dto.MessageDTO;
import com.hit.telemetry_parser.entity.PacketEntity;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
@ServerEndpoint(value = "/websocket/{username}/{topic}", encoders = {ServerEncoder.class})
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 记录当前用户与订阅主题的对应关系
     * map<主题id, List<username>>
     */
    public static final Map<String, List<String>> topicMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("topic") String topic) {
        sessionMap.put(username, session);
        log.info("有新订阅，username={}, topic={}, 当前在线人数为：{}", username, topic, sessionMap.size());
        if (!topicMap.isEmpty()) {
            if (topicMap.containsKey(topic)) {
                if (!topicMap.get(topic).contains(username)) {
                    List<String> users = topicMap.get(topic);
                    users.add(username);
                    topicMap.put(topic, users);
                }
            } else {
                topicMap.put(topic, List.of(username));
            }
        } else {
            topicMap.put(topic, List.of(username));
        }
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            // {"username", "zhang", "username": "admin"}
            array.add(jsonObject);
        }
//        {"users": [{"username": "zhang"},{ "username": "admin"}]}
        // 后台发送消息给所有的客户端
//        sendAllMessage(JSONUtil.toJsonStr(result));

        if (!Objects.equals(topic, WebsocketConstant.NOTIFY)) {
            // TODO: 推送遥测消息

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("topic") String topic) {
//        sessionMap.remove(username);
        if (!topicMap.isEmpty()) {
            List<String> users = topicMap.get(topic);
//            users.remove(username);
            if (users.isEmpty()) {
                topicMap.remove(topic);
            } else {
                topicMap.put(topic, users);
            }
        }
        log.debug("有一连接关闭，移除username={}, topic={}的用户session, 当前在线订阅人数为：{}", username, topic, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.debug("服务端收到用户username={}的消息:{}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUsername = obj.getStr("to"); // to表示发送给哪个用户，比如 admin
        String text = obj.getStr("text"); // 发送的消息文本  hello
        // {"to": "admin", "text": "聊天文本"}
        Session toSession = sessionMap.get(toUsername); // 根据 to用户名来获取 session，再通过session发送消息文本
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username);  // from 是 zhang
            jsonObject.set("text", "pong");  // text 同上面的text
            this.pong(jsonObject.toString(), toSession);
            log.debug("发送给用户username={}，消息：{}", toUsername, jsonObject);
        } else {
            log.error("发送失败，未找到用户username={}的session", toUsername);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void pong(String message, Session toSession) {
        try {
//            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    private void sendMessage(String result, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), result);
            toSession.getBasicRemote().sendText(result);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    public void sendAllMessage(MessageDTO message) {
        sendAllMessage(JSONUtil.toJsonStr(message));
    }

    public void sendTelemetryMessage(List<PacketEntity> list, Long topic) {
        List<String> userList = topicMap.get(topic.toString());
        if (userList.isEmpty()) {
            log.warn("无实时推送订阅用户");
        } else {
            String message = JSONUtil.toJsonStr(list);
            userList.forEach(user -> sendMessage(message, sessionMap.get(user)));
        }
    }
}

