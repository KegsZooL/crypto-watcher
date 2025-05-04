package com.github.kegszool.notificaiton;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@Component
public class NotificationActionExecutor {

    private final NotificationProducer producer;
    private final ActiveNotificationCacheService activeNotificationCache;
    private final TriggeredNotificationBuffer triggeredBuffer;

    @Autowired
    public NotificationActionExecutor(
            NotificationProducer producer,
            ActiveNotificationCacheService activeNotificationCache,
            TriggeredNotificationBuffer triggeredBuffer
    ) {
        this.producer = producer;
        this.activeNotificationCache = activeNotificationCache;
        this.triggeredBuffer = triggeredBuffer;
    }

    public void execute(NotificationDto notification, String coinName, BigDecimal currentPrice) {
        notification.setTriggered(true);
        producer.sendTriggeredNotification(notification);

        log.info("Notification triggered | Coin: {} | Chat ID: {} | Current price: {}",
                coinName, notification.getChatId(), currentPrice);

        if (notification.isRecurring()) {
            handleRecurring(notification, coinName, currentPrice);
        } else {
            handleOneTime(notification, coinName);
        }
    }

    private void handleRecurring(NotificationDto notification, String coinName, BigDecimal currentPrice) {
        notification.setInitialPrice(currentPrice.doubleValue());
        notification.setTriggered(false);
        activeNotificationCache.add(coinName, List.of(notification));
        log.info("Recurring updated | Coin: {} | New base price: {} | Chat ID: {}",
                coinName, currentPrice, notification.getChatId());
    }

    private void handleOneTime(NotificationDto notification, String coinName) {
        activeNotificationCache.remove(coinName, notification);
        triggeredBuffer.add(notification);
    }
}