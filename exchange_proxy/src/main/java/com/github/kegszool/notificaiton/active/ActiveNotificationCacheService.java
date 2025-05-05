package com.github.kegszool.notificaiton.active;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.github.kegszool.messaging.dto.NotificationDto;

@Log4j2
@Service
public class ActiveNotificationCacheService {

    private final ConcurrentMap<String, CopyOnWriteArrayList<NotificationDto>> cache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, NotificationDto> pendingRemovals = new ConcurrentHashMap<>();

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
        pendingRemovals.put(buildKey(triggered), triggered);
        cache.computeIfPresent(coinName, (k, list) -> {
            list.remove(triggered);
            return list;
        });
    }

    public void confirmRemoval(NotificationDto notification) {
        String key = buildKey(notification);
        NotificationDto removed = pendingRemovals.remove(key);
        if (removed == null) {
            log.warn("Received confirmation for non-pending removal: {}", key);
        }
    }

    private String buildKey(NotificationDto not) {
        return not.getChatId() + "_" + not.getCoin().getName() + "_"
                + not.getMessageId() + "_" + not.getTargetPercentage() + "_"
                + not.getDirection();
    }
}