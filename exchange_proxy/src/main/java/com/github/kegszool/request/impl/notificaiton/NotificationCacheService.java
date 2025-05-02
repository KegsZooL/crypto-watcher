package com.github.kegszool.request.impl.notificaiton;

import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationCacheService {

    private final Map<String, List<NotificationDto>> cache = new ConcurrentHashMap<>();

    public List<NotificationDto> getNotifications(String coinName) {
        return cache.getOrDefault(coinName, Collections.emptyList());
    }

    public void updateNotifications(String coinName, List<NotificationDto> notifications) {
        cache.put(coinName, notifications);
    }

    public void removeNotification(String coinName, NotificationDto triggered) {
        cache.computeIfPresent(coinName, (k, v) -> {
            v.remove(triggered);
            return v;
        });
    }
}