package com.github.kegszool.notificaiton.util;

import java.math.RoundingMode;
import java.util.List;
import java.math.BigDecimal;

import com.github.kegszool.notificaiton.NotificationActionExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class NotificationTriggerChecker {

    private final ActiveNotificationCacheService activeNotificationCache;
    private final NotificationTriggerEvaluator evaluator;
    private final NotificationActionExecutor actionExecutor;

    @Autowired
    public NotificationTriggerChecker(
            ActiveNotificationCacheService activeNotificationCache,
            NotificationTriggerEvaluator evaluator,
            NotificationActionExecutor actionExecutor
    ) {
        this.activeNotificationCache = activeNotificationCache;
        this.evaluator = evaluator;
        this.actionExecutor = actionExecutor;
    }

    public void check(String coinName, double currentPrice) {
        List<NotificationDto> notifications = activeNotificationCache.getNotifications(coinName);

        for (NotificationDto notification : notifications) {
            if (notification.isTriggered() && !notification.isRecurring()) continue;

            logNotificationDetails(notification, coinName, currentPrice);

            if (evaluator.isTriggered(notification, currentPrice)) {
                actionExecutor.execute(notification, coinName, currentPrice);
            }
        }
    }

    private void logNotificationDetails(NotificationDto notification, String coinName, double currentPrice) {
        double initialPrice = notification.getInitialPrice();
        double targetPercent = notification.getTargetPercentage().doubleValue();
        double targetChange = initialPrice * targetPercent / 100.0;
        double targetPrice = switch (notification.getDirection()) {
            case Up -> initialPrice + targetChange;
            case Down -> initialPrice - targetChange;
        };

        log.info("Notification check | Coin: {} | Chat ID: {} | Direction: {} | Initial: {} | %: {} | Target: {} | Current: {}",
                coinName, notification.getChatId(), notification.getDirection(),
                initialPrice, targetPercent, String.format("%.2f", targetPrice), currentPrice);
    }
}