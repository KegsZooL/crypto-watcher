package com.github.kegszool.notificaiton.subscription;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.websocket.connection.OkxWebSocketConnector;

import com.github.kegszool.websocket.util.WebSocketSubscriptionTracker;
@Service
public class NotificationWebSocketSubscriber {

    private final WebSocketSubscriptionTracker subscriptionTracker;
    private final OkxWebSocketConnector webSocketConnector;
    private final ActiveNotificationCacheService activeNotifications;

    private final String currencySuffix;

    @Autowired
    public NotificationWebSocketSubscriber(
            WebSocketSubscriptionTracker subscriptionTracker,
            OkxWebSocketConnector webSocketConnector,
            ActiveNotificationCacheService activeNotifications,
            @Value("${currency_suffix}") String currencySuffix
    ) {
        this.subscriptionTracker = subscriptionTracker;
        this.webSocketConnector = webSocketConnector;
        this.activeNotifications = activeNotifications;
        this.currencySuffix = currencySuffix;
    }

    public void subscribe(List<NotificationDto> notifications) {

        Map<String, List<NotificationDto>> groupedByCoin = notifications.stream()
                .collect(Collectors.groupingBy(n -> n.getCoin().getName()));

        for (Map.Entry<String, List<NotificationDto>> entry : groupedByCoin.entrySet()) {
            String coinName = entry.getKey();
            List<NotificationDto> coinNotifications = entry.getValue();

            activeNotifications.add(coinName, coinNotifications);

            String fullCoinName = coinName + currencySuffix;

            if (subscriptionTracker.increment(coinName)) {
                webSocketConnector.connect(fullCoinName);
            }
        }
    }

    public void unsubscribe(NotificationDto deleted) {
        String coinName = deleted.getCoin().getName();
        String fullCoinName = coinName + currencySuffix;

        if (subscriptionTracker.decrement(coinName)) {
            webSocketConnector.disconnect(fullCoinName);
        }
        activeNotifications.remove( deleted);
    }
}