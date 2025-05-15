package com.github.kegszool.websocket.session;

import jakarta.websocket.Session;

public interface WebSocketSessionManager {
    void addSession(String instId, Session session);
    void removeSession(String instId);
}