package com.github.kegszool.websocket.connection;

import java.net.URI;
import java.util.Map;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.websocket.Session;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import jakarta.websocket.DeploymentException;

import com.github.kegszool.websocket.WebSocketClient;
import com.github.kegszool.websocket.WebSocketMessageHandler;
import com.github.kegszool.websocket.session.WebSocketSessionManager;

@Log4j2
@Component
public class OkxWebSocketConnector implements WebSocketConnector<String> {

    private final String url;
    private final String subscribeTemplate;
    private final WebSocketSessionManager sessionManager;
    private final WebSocketMessageHandler messageHandler;

    private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @Autowired
    public OkxWebSocketConnector( @Value("${api.exchange.url.websocket}") String url,
                                  @Value("${api.exchange.websocket.subscribe-template}") String subscribeTemplate,
            WebSocketSessionManager sessionManager,
            WebSocketMessageHandler messageHandler
    ) {
        this.url = url;
        this.subscribeTemplate = subscribeTemplate;
        this.sessionManager = sessionManager;
        this.messageHandler = messageHandler;
    }

    @Override
    public void connect(String instId) {
        if (activeSessions.containsKey(instId)) {
            log.debug("Already connected to instId: {}", instId);
            return;
        }
        try {
            processConnect(instId);
        } catch (URISyntaxException ex) {
            log.error("Invalid WebSocket URI: {}", url, ex);
        } catch (Exception ex) {
            log.error("Failed to establish WebSocket connection for instId: {}", instId, ex);
        }
    }

    private void processConnect(String instId) throws URISyntaxException, IOException, DeploymentException {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI(url);

            WebSocketClient client = new WebSocketClient(instId, sessionManager, messageHandler);
            Session session = container.connectToServer(client, uri);
            activeSessions.put(instId, session);

            log.info("WebSocket connection established for instId: {}", instId);
            subscribeToTicker(instId, session);
    }

    private void subscribeToTicker(String instId, Session session) {
        try {
            String subscribeMessage = String.format(subscribeTemplate, instId);
            session.getBasicRemote().sendText(subscribeMessage);
            log.info("Sent subscribe message for instId {}: {}", instId, subscribeMessage);
        } catch (IOException ex) {
            log.error("Failed to send subscribe message for instId: {}", instId, ex);
        }
    }

    @Override
    public void disconnect(String instId) {
        Session session = activeSessions.get(instId);
        if (session != null) {
            try {
                session.close();
                activeSessions.remove(instId);
                log.info("Disconnected from WebSocket for instId: {}", instId);
            } catch (Exception ex) {
                log.error("Failed to close WebSocket session for instId: {}", instId, ex);
            }
        } else {
            log.debug("No active WebSocket session found for instId: {}", instId);
        }
    }
}