package com.github.kegszool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.messaging.dto.command_entity.Direction;
import com.github.kegszool.messaging.dto.command_entity.NotificationDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Log4j2
@Service
@Component
public class OkxPriceWebSocketHandler extends TextWebSocketHandler {


    private final static String CHANNEL = "tickers";
    private final static String ROW_SUBSCRIPTION_KEY = CHANNEL + "-";
    private final static int SUBSCRIBE_TIMEOUT = 5;

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketConnectionManager> instIdToConnectionManager = new ConcurrentHashMap<>();
    private final String webSocketUrl;

    public OkxPriceWebSocketHandler(
            ObjectMapper objectMapper,
            @Value("${api.exchange.url.websocket}") String webSocketUrl
    ) {
        this.objectMapper = objectMapper;
        this.webSocketUrl = webSocketUrl;
    }

    public void subscribe(String instId) {

        String subKey = ROW_SUBSCRIPTION_KEY + instId;

        if (instIdToConnectionManager.containsKey(instId)) {
            return;
        }
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                this,
                webSocketUrl
        );
        log.info("Subscribed to {} updates", instId);;
        manager.setAutoStartup(true);
        manager.start();
        instIdToConnectionManager.put(subKey, manager);

        try {
            waitForConnection(manager, SUBSCRIBE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            //TODO: logging
            return;
        }

        try {
            WebSocketSession session = activeSessions.get(webSocketUrl);
            if (session != null && session.isOpen()) {
                Map<String, Object> subscribeMsg = new HashMap<>();
                subscribeMsg.put("op", "subscribe");
                subscribeMsg.put("args", Collections.singleton(Map.of(
                        "channel", CHANNEL,
                        "instId", instId
                )));

                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(subscribeMsg)));
            }
        } catch (IOException ex) {
            log.error("Failed to send subscription message for {}", instId, ex);
        }
    }

    private void waitForConnection(
            WebSocketConnectionManager manager,
            long timeout, TimeUnit unit
    ) throws InterruptedException, TimeoutException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        while (System.currentTimeMillis() < endTime) {
            if (manager.isConnected()) {
                return;
            }
            Thread.sleep(100);
        }
        throw new TimeoutException("WebSocket connection timeout");
    }

    public void unsubscribe(String instId) {
        String subKey = ROW_SUBSCRIPTION_KEY + instId;
        WebSocketConnectionManager manager = instIdToConnectionManager.get(subKey);

        if (manager != null) {
            try {
                WebSocketSession session = activeSessions.get(subKey);
                if (session != null && session.isOpen()) {

                    Map<String, Object> unsubscribeMsg = new HashMap<>();
                    unsubscribeMsg.put("op", "unsubscribe");
                    unsubscribeMsg.put("args", Collections.singleton(Map.of(
                            "channel", CHANNEL,
                            "instId", instId
                            )
                    ));
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(unsubscribeMsg)));
                    log.info("Unsubscribed from {} updates", instId);
                }
            } catch(IOException ex) {
                log.error("Failed to send unsubscribe message for {}", instId, ex);
            } finally {
                manager.stop();
                instIdToConnectionManager.remove(subKey);
                log.info("WebSocket connection for {} stopped", instId);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("Received WebSocket message: {}", payload);

        try {
            Map<String, Object> response = objectMapper.readValue(payload, Map.class);
            if (response.containsKey("arg")) {
                Map<String, Object> arg = (Map<String, Object>) response.get("arg");
                if ("tickers".equals(arg.get("channel"))) {
                    if (response.containsKey("data")) {
                        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
                        if (dataList != null && !dataList.isEmpty()) {
                            Map<String, Object> tickerData = dataList.get(0);
                            String instId = (String) tickerData.get("instId");
                            String lastPriceStr = (String) tickerData.get("last");
                            BigDecimal lastPrice = new BigDecimal(lastPriceStr);

                            checkAndSendNotifications(instId, lastPrice);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error processing WebSocket message", ex);
        }
    }

    private void checkAndSendNotifications(String instId, BigDecimal currentPrice) {

//        List<NotificationDto> notifications = getActiveNotificationsForCoin(instId);
//
//        for (NotificationDto notification : notifications) {
//            if (shouldNotify(notification, currentPrice)) {
////                sendTelegramNotification(notification, currentPrice);
//
//                if (!notification.isRecurring()) {
////                    deactivateNotification(notification);
//                }
//            }
//        }
    }

    private boolean shouldNotify(NotificationDto notification, BigDecimal currentPrice) {
        BigDecimal targetPrice = notification.getTargetPrice();
        Direction direction = notification.getDirection();

        if (targetPrice != null) {
            switch (direction) {
                case Up:
                    return currentPrice.compareTo(targetPrice) >= 0;
                case Down:
                    return currentPrice.compareTo(targetPrice) <= 0;
                default:
                    return currentPrice.compareTo(targetPrice) == 0;
            }
        }
        return false;
    }
}