package com.github.kegszool.request.impl.notificaiton;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.kegszool.messaging.dto.NotificationDto;

import java.util.List;

@Log4j2
@Component
public class NotificationCheckerService {

    private final NotificationCacheService cacheService;

    @Autowired
    public NotificationCheckerService(NotificationCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void check(String coinName, double currentPrice) {
        List<NotificationDto> notifications = cacheService.getNotifications(coinName);

        for (NotificationDto notification : notifications) {
            if (notification.isTriggered()) continue;

            double targetChange = notification.getInitialPrice() * notification.getTargetPercentage().doubleValue() / 100;
            boolean conditionMet = switch (notification.getDirection()) {
                case Up -> currentPrice >= notification.getInitialPrice() + targetChange;
                case Down -> currentPrice <= notification.getInitialPrice() - targetChange;
            };

            if (conditionMet) {
                notification.setTriggered(true);

                cacheService.removeNotification(coinName, notification);
                log.info("Notification triggered for chatId {} and coin {}", notification.getChatId(), coinName);
            }
        }
    }
}
