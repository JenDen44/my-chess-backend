package com.chess.jnd.notification;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionService {

    private final Map<WebSocketSession, String> userSessions = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session, String token) {
        userSessions.put(session, token);
    }

    public void removeSession(WebSocketSession session) {
        userSessions.remove(session);
    }

    public <TData> void sendMessage(TData data, String... tokens) {
        var payload = new WebSocketPayload(data);

        for (String token : tokens) {
            for (Map.Entry<WebSocketSession, String> entry : userSessions.entrySet()) {
                if (entry.getValue().equals(token)) {
                    try {
                        entry.getKey().sendMessage(payload.toTextMessage());
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
