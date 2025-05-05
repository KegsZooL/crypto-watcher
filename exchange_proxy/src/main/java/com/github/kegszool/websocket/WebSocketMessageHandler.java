package com.github.kegszool.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.notificaiton.util.NotificationTriggerChecker;

@Log4j2
@Component
public class WebSocketMessageHandler {

    private final String currencySuffix;
    private final NotificationTriggerChecker notificationChecker;

    @Autowired
    public WebSocketMessageHandler(
            @Value("${currency_suffix}") String currencySuffix,
            NotificationTriggerChecker notificationChecker
    ) {
        this.currencySuffix = currencySuffix;
        this.notificationChecker = notificationChecker;
    }

    @Async
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

            String coinName = instId.replace(currencySuffix, "");
            notificationChecker.check(coinName, price);

        } catch (Exception e) {
            log.warn("Error handling WebSocket message: {}", message, e);
        }
    }
}