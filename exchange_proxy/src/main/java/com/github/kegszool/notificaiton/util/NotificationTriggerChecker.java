package com.github.kegszool.notificaiton.util;

import java.util.Map;
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

    private final static int DELAY = 60000;
    private final Map<String, Long> lastTriggerPerChat = new ConcurrentHashMap<>();

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

            long chatId = notification.getChatId();
            String key = chatId + ":" + coinName;
            long lastTime = lastTriggerPerChat.getOrDefault(key, 0L);

            if ((now - lastTime) < DELAY) continue;

            if (evaluator.isTriggered(notification, currentPrice)) {
                notification.setLastTriggeredTime(now);
                lastTriggerPerChat.put(key, now);
                actionExecutor.execute(notification, coinName, currentPrice);
            }
        }
    }
}