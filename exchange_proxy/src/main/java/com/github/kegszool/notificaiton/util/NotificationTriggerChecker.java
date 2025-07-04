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

    private final static int DELAY = 5000;
    private final Map<String, Long> lastTriggerPerChat = new ConcurrentHashMap<>();
    private final Set<String> processingNotificationKeys = ConcurrentHashMap.newKeySet();

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
        List<NotificationDto> originalNotifications = activeNotificationCache.getNotifications(coinName);
        long now = System.currentTimeMillis();

        for (NotificationDto original : originalNotifications) {
            NotificationDto notification = copy(original);

            if (notification.isTriggered()) continue;

            String key = generateKey(notification, coinName);

            boolean acquired = processingNotificationKeys.add(key);
            if (!acquired) {
                log.debug("Notification already being processed concurrently, skipping: {}", key);
                continue;
            }

            try {
                synchronized (key.intern()) {
                    long lastTime = lastTriggerPerChat.getOrDefault(key, 0L);
                    if ((now - lastTime) < DELAY) {
                        log.info("Skipping notification due to debounce | Chat: {} | Delay: {} ms",
                                notification.getChatId(), DELAY);
                        continue;
                    }

                    if (evaluator.isTriggered(notification, currentPrice)) {
                        NotificationDto updated = copy(notification);
                        updated.setLastTriggeredTime(now);
                        updated.setTriggered(true);

                        actionExecutor.execute(updated, coinName, currentPrice, now);
                        lastTriggerPerChat.put(key, now);
                    }
                }
            } finally {
                processingNotificationKeys.remove(key);
            }
        }
    }

    private NotificationDto copy(NotificationDto original) {
        return new NotificationDto(
                original.getUser(),
                original.getMessageId(),
                original.getChatId(),
                original.getCoin(),
                original.isRecurring(),
                original.isTriggered(),
                original.getInitialPrice(),
                original.getTriggeredPrice(),
                original.getTargetPercentage(),
                original.getDirection(),
                original.getLastTriggeredTime()
        );
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
}