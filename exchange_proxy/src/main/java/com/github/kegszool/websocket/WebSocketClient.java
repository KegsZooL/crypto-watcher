package com.github.kegszool.websocket;

import com.github.kegszool.websocket.connection.OkxWebSocketConnector;
import jakarta.websocket.*;
import lombok.extern.log4j.Log4j2;
import com.github.kegszool.websocket.session.WebSocketSessionManager;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@ClientEndpoint
public class WebSocketClient {

    private final String instId;
    private final WebSocketSessionManager sessionManager;
    private final WebSocketMessageHandler messageHandler;
    private final OkxWebSocketConnector connector;

    private volatile Instant lastMessageReceived = Instant.now();
    private final ScheduledExecutorService pingScheduler = Executors.newSingleThreadScheduledExecutor();

    private Session session;

    public WebSocketClient(
            String instId,
            WebSocketSessionManager sessionManager,
            WebSocketMessageHandler messageHandler,
            OkxWebSocketConnector connector
    ) {
        this.instId = instId;
        this.sessionManager = sessionManager;
        this.messageHandler = messageHandler;
        this.connector = connector;

        startPingCheck();
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sessionManager.addSession(instId, session);
        lastMessageReceived = Instant.now();
    }

    @OnMessage
    public void onMessage(String message) {
        lastMessageReceived = Instant.now();

        if (message.contains("pong")) {
            log.info("Received pong for instId {}", instId);
        } else {
            messageHandler.handle(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("WebSocket error for instId '{}': {}", instId, throwable.getMessage(), throwable);
        reconnect();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessionManager.removeSession(instId);
        log.info("WebSocket closed for instId '{}': {}", instId, closeReason);
        reconnect();
    }

    private void startPingCheck() {
        pingScheduler.scheduleAtFixedRate(() -> {
            if (session == null || !session.isOpen()) return;

            Duration sinceLastMessage = Duration.between(lastMessageReceived, Instant.now());
            if (sinceLastMessage.getSeconds() >= 25) {
                try {
                    session.getAsyncRemote().sendText("ping");
                    log.info("Sent ping for instId {}", instId);
                } catch (Exception e) {
                    log.info("Failed to send ping for instId {}: {}", instId, e.getMessage());
                }

                Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                    if (Duration.between(lastMessageReceived, Instant.now()).getSeconds() > 30) {
                        log.info("No pong received for instId {} — reconnecting", instId);
                        reconnect();
                    }
                }, 5, TimeUnit.SECONDS);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void reconnect() {
        connector.disconnect(instId);
        connector.connect(instId);
    }
}