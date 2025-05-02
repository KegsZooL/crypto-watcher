package com.github.kegszool.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.request.impl.notificaiton.NotificationCheckerService;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@Component
public class WebSocketMessageHandler {

    private final NotificationCheckerService notificationChecker;

    @Autowired
    public WebSocketMessageHandler(
            NotificationCheckerService notificationChecker
    ) {
        this.notificationChecker = notificationChecker;
    }

    public void handle(String message) {
        try {
            JsonNode root = new ObjectMapper().readTree(message);

            if (root.has("event")) {
                log.info("WebSocket control message received: {}", message);
                return;
            }

            JsonNode argNode = root.path("arg");
            JsonNode dataNode = root.path("data");

            if (!argNode.has("instId") || !dataNode.isArray() || dataNode.isEmpty()) {
                log.debug("Invalid ticker message structure: {}", message);
                return;
            }

            String instId = argNode.get("instId").asText();
            String last = dataNode.get(0).path("last").asText();

            double price = Double.parseDouble(last);

            notificationChecker.check(instId, price);

        } catch (Exception e) {
            log.warn("Error handling WebSocket message: {}", message, e);
        }
    }
}