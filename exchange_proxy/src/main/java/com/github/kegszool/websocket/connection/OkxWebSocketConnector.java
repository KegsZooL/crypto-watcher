package com.github.kegszool.websocket.connection;

import com.github.kegszool.websocket.WebSocketClient;
import com.github.kegszool.websocket.WebSocketMessageHandler;
import com.github.kegszool.websocket.session.WebSocketSessionManager;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class OkxWebSocketConnector implements WebSocketConnector<String> {

    private final String url;
    private final WebSocketSessionManager sessionManager;
    private final WebSocketMessageHandler messageHandler;

    private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @Autowired
    public OkxWebSocketConnector(
            @Value("${api.exchange.url.websocket}") String url,
            WebSocketSessionManager sessionManager,
            WebSocketMessageHandler messageHandler
    ) {
        this.url = url;
        this.sessionManager = sessionManager;
        this.messageHandler = messageHandler;
    }

    @Override
    public void connect(String instId) {
        if (activeSessions.containsKey(instId)) return;

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI(url);

            WebSocketClient client = new WebSocketClient(instId, sessionManager, messageHandler);
            Session session = container.connectToServer(client, uri);
            activeSessions.put(instId, session);

            subscribeToTicker(instId, session);

        } catch (URISyntaxException ex) {
            log.error(ex);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void subscribeToTicker(String instId, Session session) {
        try {
            String subscribeMessage = String.format(
                    "{\"op\":\"subscribe\",\"args\":[{\"channel\":\"tickers\",\"instId\":\"%s\"}]}",
                    instId
            );
            session.getBasicRemote().sendText(subscribeMessage);
        } catch (Exception ex) {

        }
    }

    @Override
    public void disconnect(String instId) {
        Session session = activeSessions.get(instId);
        if (session != null) {
            try {
                session.close();
                activeSessions.remove(instId);
            } catch (Exception ex) {

            }
        }
    }
}