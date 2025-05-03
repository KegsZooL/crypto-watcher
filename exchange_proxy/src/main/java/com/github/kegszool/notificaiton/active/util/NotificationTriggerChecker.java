package com.github.kegszool.notificaiton.active.util;

import java.util.List;

import com.github.kegszool.notificaiton.NotificationUpdateBuffer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.NotificationProducer;
import com.github.kegszool.notificaiton.NotificationCacheService;

@Log4j2
@Component
public class NotificationTriggerChecker {

    private final NotificationCacheService cacheService;
    private final NotificationProducer producer;
    private final NotificationUpdateBuffer updateBuffer;

    @Autowired
    public NotificationTriggerChecker(
            NotificationCacheService cacheService,
            NotificationProducer producer,
            NotificationUpdateBuffer updateBuffer
    ) {
        this.cacheService = cacheService;
        this.producer = producer;
        this.updateBuffer = updateBuffer;
    }

    public void check(String coinName, double currentPrice) {

        List<NotificationDto> notifications = cacheService.getNotifications(coinName);

        for (NotificationDto notification : notifications) {

            if (notification.isTriggered() && !notification.isRecurring()) continue;
            boolean triggered = isTriggered(notification, currentPrice);

            if (triggered) {
                notification.setTriggered(true);
                producer.sendTriggeredNotification(notification);
                log.info("Notification triggered for coin '{}' | Chat id: '{}'", coinName, notification.getChatId());

                if (notification.isRecurring()) {

                    notification.setInitialPrice(currentPrice);
                    notification.setTriggered(false);
//                    updateBuffer.add(notification);
                } else {
                    log.info("Recurring notification for coin '{}' updated for next trigger | New base price: {} | Chat id: '{}'",
                            coinName, currentPrice, notification.getChatId());
                	cacheService.removeNotification(coinName, notification);
//                    updateBuffer.add(notification);
                }
            }
        }
    }

    private boolean isTriggered(NotificationDto notification, double currentPrice) {
        double targetChange = notification.getInitialPrice() * notification.getTargetPercentage().doubleValue() / 100;
        return switch (notification.getDirection()) {
            case Up -> currentPrice >= notification.getInitialPrice() + targetChange;
            case Down -> currentPrice <= notification.getInitialPrice() - targetChange;
        };
    }
}