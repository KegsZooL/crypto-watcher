package com.github.kegszool.notificaiton.util;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.NotificationActionExecutor;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class NotificationTriggerChecker {

    private final Set<String> processingNotifcationKeys = ConcurrentHashMap.newKeySet();

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
        long now = System.currentTimeMillis();

        for (NotificationDto notification : notifications) {
            if (notification.isTriggered()) continue;

            String key = generateKey(notification, coinName);
            boolean isAdded = processingNotifcationKeys.add(key);
            if(!isAdded) {
                log.debug("Notification already being processed, skipping: {}", key);
                continue;
            }
            try {
            	logNotificationDetails(notification, coinName, currentPrice);
                if (evaluator.isTriggered(notification, currentPrice)) {
                    notification.setLastTriggeredTime(System.currentTimeMillis());
                    actionExecutor.execute(notification, coinName, currentPrice);
                }
            } finally {
                processingNotifcationKeys.remove(key);
            }
        }
    }

    private String generateKey(NotificationDto notification, String coinName) {
        return String.format("%d_%s_%s_%.4f_%b",
                notification.getChatId(),
                coinName,
                notification.getDirection(),
                notification.getTargetPercentage(),
                notification.isRecurring()
        );
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
                initialPrice, targetPercent, targetPrice, currentPrice);
    }
}