package com.bzw.api.web;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author yanbin
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocket {

    private static int onlineCount = 0;

    private static Map<String, WebSocket> webSocketMap = Maps.newHashMap();

    private Session session;

    private static final String KEY = "id";

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String[] strs = session.getQueryString().split("=");
        if (KEY.equals(strs[0])) {
            webSocketMap.put(strs[1], this);
            addOnlineCount();
            System.out.println("有新链接加入!当前在线人数为" + getOnlineCount());
        }
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this);
        subOnlineCount();
        System.out.println("有一链接关闭!当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("来自客户端的消息:" + message);
    }

    public static void sendMessage(String key, String message) throws IOException {
        WebSocket socket = webSocketMap.get(key);
        if (socket!=null) {
            socket.sendMessage(message);
        }
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized int getOnlineCount() {
        return WebSocket.onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocket that = (WebSocket) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
