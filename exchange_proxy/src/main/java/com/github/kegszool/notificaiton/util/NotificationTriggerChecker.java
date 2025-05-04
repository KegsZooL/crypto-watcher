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

    public void check(String coinName, double currentPriceDouble) {
        BigDecimal currentPrice = BigDecimal.valueOf(currentPriceDouble);
        List<NotificationDto> notifications = activeNotificationCache.getNotifications(coinName);

        for (NotificationDto notification : notifications) {

            if (notification.isTriggered() && !notification.isRecurring()) continue;

            logNotificationDetails(notification, coinName, currentPrice);

            if (evaluator.isTriggered(notification, currentPrice)) {
               actionExecutor.execute(notification, coinName, currentPrice);
            }
        }
    }

    private void logNotificationDetails(NotificationDto notification, String coinName, BigDecimal currentPrice) {
        BigDecimal initialPrice = BigDecimal.valueOf(notification.getInitialPrice());
        BigDecimal targetPercent = notification.getTargetPercentage();
        BigDecimal targetChange = initialPrice
                .multiply(targetPercent)
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);
        BigDecimal targetPrice = switch (notification.getDirection()) {
            case Up -> initialPrice.add(targetChange);
            case Down -> initialPrice.subtract(targetChange);
        };

        log.info("🔍 Notification check | Coin: {} | Chat ID: {} | Direction: {} | Initial: {} | %: {} | Target: {} | Current: {}",
                coinName, notification.getChatId(), notification.getDirection(),
                initialPrice, targetPercent, targetPrice, currentPrice);
    }
}