package com.github.kegszool.websocket.session;

import java.util.Map;
import jakarta.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSessionManagerImpl implements WebSocketSessionManager {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void addSession(String instId, Session session) {
        sessions.put(instId, session);
    }

    @Override
    public Session getSession(String instId) {
        return sessions.get(instId);
    }

    @Override
    public void removeSession(String instId) {
        sessions.remove(instId);
    }
}