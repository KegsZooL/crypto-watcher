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
        processOldNotification(notification);
        processNewNotification(notification, coinName, currentPrice);
    }

    private void processOldNotification(NotificationDto notification) {
        activeNotificationCache.remove(notification);
        triggeredNotificationBuffer.add(notification);
    }

    private void processNewNotification(NotificationDto notification, String coinName, double currentPrice) {

        NotificationDto newNotification = new NotificationDto(
                notification.getUser(), notification.getMessageId(),
                notification.getChatId(), notification.getCoin(),
                notification.isRecurring(), false,
                notification.getTriggeredPrice(), currentPrice,
                notification.getTargetPercentage(), notification.getDirection(),
                notification.getLastTriggeredTime()
        );
        activeNotificationCache.add(coinName, List.of(newNotification));

        log.info("Recurring updated | Coin: {} | New base price: {} | Chat ID: {}",
                coinName, currentPrice, newNotification.getChatId());
    }
}