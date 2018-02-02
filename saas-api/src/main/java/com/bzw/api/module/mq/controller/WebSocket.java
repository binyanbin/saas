package com.bzw.api.module.mq.controller;

import com.bzw.common.utils.HttpClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yanbin
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocket {

    private static final String pushUrl = "mq/push?receiver=";

    private static int onlineCount = 0;

    private static Map<String, List<WebSocket>> webSocketMap = Maps.newHashMap();

    private Session session;

    private static final String KEY = "id";

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String[] strArray = session.getQueryString().split("=");
        if (KEY.equals(strArray[0])) {
            if (webSocketMap.containsKey(strArray[1])) {
                List<WebSocket> webSockets = webSocketMap.get(strArray[1]);
                webSockets.add(this);
            } else {
                List<WebSocket> webSockets = Lists.newArrayList();
                webSockets.add(this);
                webSocketMap.put(strArray[1], webSockets);
            }
            addOnlineCount();
            String url = "http://" + session.getRequestURI().getAuthority() + "/" + pushUrl + strArray[1];
            HttpClient.get(url);
        }
    }

    @OnClose
    public void onClose() {
        subOnlineCount();
        String[] strArray = session.getQueryString().split("=");
        if (strArray.length >= 2) {
            List<WebSocket> webSockets = webSocketMap.get(strArray[1]);
            for (WebSocket webSocket : webSockets) {
                if (webSocket.equals(this)) {
                    webSockets.remove(this);
                    break;
                }
            }
        }
    }


    @OnMessage
    public void onMessage(String message) {
        System.out.println("来自客户端的消息:" + message);
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

    public static boolean sendMessage(String key, String message) {
        List<WebSocket> sockets = webSocketMap.get(key);
        boolean result = false;
        if (!CollectionUtils.isEmpty(sockets)) {
            for (int i = 0; i < sockets.size(); i++) {
                try {
                    WebSocket webSocket = sockets.get(i);
                    webSocket.sendMessage(message);
                    result = true;
                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
                    sockets.remove(i--);
                }
            }
        }
        return result;
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
}
