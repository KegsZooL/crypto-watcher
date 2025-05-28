package com.github.kegszool.notificaiton.handler;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class RecurringNotificationHandler implements NotificationHandler {

    private final ActiveNotificationCacheService activeNotificationCache;
    private final TriggeredNotificationBuffer triggeredNotificationBuffer;

    @Autowired
    public RecurringNotificationHandler(
            ActiveNotificationCacheService activeNotifications,
            TriggeredNotificationBuffer triggeredNotificationBuffer
    ) {
        this.activeNotificationCache = activeNotifications;
        this.triggeredNotificationBuffer = triggeredNotificationBuffer;
    }

    @Override
    public boolean supports(NotificationDto notification) {
        return notification.isRecurring();
    }

    @Override
    public void handle(NotificationDto notification, String coinName, double currentPrice) {

        triggeredNotificationBuffer.add(notification);
        activeNotificationCache.remove(notification);

        NotificationDto updated = new NotificationDto(
                notification.getUser(),
                notification.getMessageId(),
                notification.getChatId(),
                notification.getCoin(),
                true,
                false,
                currentPrice,
                notification.getTriggeredPrice(),
                notification.getTargetPercentage(),
                notification.getDirection(),
                System.currentTimeMillis()
        );

        log.info("Recurring notification has been updated | Coin: {} | New base price: {} | Target percentage: {} | Direction: {}",
                coinName, currentPrice, updated.getTargetPercentage(), updated.getDirection()
        );
        activeNotificationCache.add(coinName, List.of(updated));
    }
}