package com.github.kegszool.notificaiton.handler;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class RecurringNotificationHandler implements NotificationHandler {

    private final ActiveNotificationCacheService activeNotificationCache;

    @Autowired
    public RecurringNotificationHandler(ActiveNotificationCacheService activeNotificationCache) {
        this.activeNotificationCache = activeNotificationCache;
    }

    @Override
    public boolean supports(NotificationDto notification) {
        return notification.isRecurring();
    }

    @Override
    public void handle(NotificationDto notification, String coinName, double currentPrice) {
        notification.setInitialPrice(currentPrice);
        notification.setTriggered(false);
        activeNotificationCache.add(coinName, List.of(notification));
        log.info("Recurring updated | Coin: {} | New base price: {} | Chat ID: {}",
                coinName, currentPrice, notification.getChatId());
    }
}