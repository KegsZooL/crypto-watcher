package com.github.kegszool.notificaiton;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import com.github.kegszool.messaging.dto.NotificationDto;

@Service
public class NotificationCacheService {

    private final Map<String, List<NotificationDto>> cache = new ConcurrentHashMap<>();

    public List<NotificationDto> getNotifications(String coinName) {
        return cache.getOrDefault(coinName, Collections.emptyList());
    }

    public void addNotifications(String coinName, List<NotificationDto> notifications) {
        cache.put(coinName, new ArrayList<>(notifications));
    }

    public void removeNotification(String coinName, NotificationDto triggered) {
        cache.computeIfPresent(coinName, (k, v) -> {
            v.remove(triggered);
            return v;
        });
    }
}