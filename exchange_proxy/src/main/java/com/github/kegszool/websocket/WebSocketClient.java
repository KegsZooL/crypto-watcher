package com.github.kegszool.websocket;

import jakarta.websocket.*;
import lombok.extern.log4j.Log4j2;
import com.github.kegszool.websocket.session.WebSocketSessionManager;

@Log4j2
@ClientEndpoint
public class WebSocketClient {

    private final String instId;
    private final WebSocketSessionManager sessionManager;
    private final WebSocketMessageHandler messageHandler;

    public WebSocketClient(
            String instId,
            WebSocketSessionManager sessionManager,
            WebSocketMessageHandler messageHandler
    ) {
        this.instId = instId;
        this.sessionManager = sessionManager;
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public void onOpen(Session session) {
        sessionManager.addSession(instId, session);
        log.info("WebSocket opened for '{}'", instId);
    }

    @OnMessage
    public void onMessage(String message) {
        messageHandler.handle(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket error for '{}': {}", instId, throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessionManager.removeSession(instId);
        log.info("WebSocket closed for '{}'", instId);
    }
}