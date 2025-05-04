package com.github.kegszool.notificaiton.active;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import com.github.kegszool.messaging.dto.NotificationDto;

@Service
public class ActiveNotificationCacheService {

    private final ConcurrentMap<String, CopyOnWriteArrayList<NotificationDto>> cache = new ConcurrentHashMap<>();

    public List<NotificationDto> getNotifications(String coinName) {
        return cache.getOrDefault(coinName, new CopyOnWriteArrayList<>());
    }

    public void add(String coinName, List<NotificationDto> notifications) {
        cache.compute(coinName, (k, existing) -> {
            if (existing == null) {
                return new CopyOnWriteArrayList<>(notifications);
            } else {
                existing.addAll(notifications);
                return existing;
            }
        });
    }

    public void remove(String coinName, NotificationDto triggered) {
        cache.computeIfPresent(coinName, (k, list) -> {
            list.remove(triggered);
            return list;
        });
    }
}