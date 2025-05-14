package com.github.kegszool.notificaiton.handler;

import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class RecurringNotificationHandler implements NotificationHandler {

    private final ActiveNotificationCacheService activeNotificationCache;
    private final TriggeredNotificationBuffer triggeredNotificationBuffer;

    @Autowired
    public RecurringNotificationHandler(ActiveNotificationCacheService activeNotificationCache, TriggeredNotificationBuffer triggeredNotificationBuffer) {
        this.activeNotificationCache = activeNotificationCache;
        this.triggeredNotificationBuffer = triggeredNotificationBuffer;
    }

    @Override
    public boolean supports(NotificationDto notification) {
        return notification.isRecurring();
    }

    @Override
    public void handle(NotificationDto notification, String coinName, double currentPrice) {
        activeNotificationCache.remove(notification);
        triggeredNotificationBuffer.add(notification);

        log.info("Recurring updated | Coin: {} | New base price: {} | Chat ID: {}",
                coinName, currentPrice, notification.getChatId());
    }
}