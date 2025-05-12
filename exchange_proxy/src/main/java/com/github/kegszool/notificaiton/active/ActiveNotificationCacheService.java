package com.github.kegszool.notificaiton.active;

import java.util.List;
import java.util.Objects;
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
                for (NotificationDto newNotification : notifications) {
                    boolean alreadyExists = existing.stream()
                            .anyMatch(existingNotification -> isSameNotification(existingNotification, newNotification));
                    if (!alreadyExists) {
                        existing.add(newNotification);
                    }
                }
                return existing;
            }
        });
    }

    public void remove(NotificationDto notification) {
        if (notification.isTriggered()) {
        	pendingRemovals.put(buildKey(notification), notification);
        }
        removeFromCache(notification);
    }

    public void confirmRemoval(NotificationDto notification) {
        String key = buildKey(notification);
        NotificationDto removed = pendingRemovals.remove(key);

        if (removed == null) {
        	log.error("Received confirmation for non-pending removal: {}", key);
        } else {
            removeFromCache(notification);
            log.info("Cache size after removal confirmation: {}", cache.get(
                    notification.getCoin().getName()
            ).size());
        }
    }

    private void removeFromCache(NotificationDto notification) {
        String coinName = notification.getCoin().getName();
        cache.computeIfPresent(coinName, (key, list) -> {
            list.removeIf(existing -> isSameNotification(existing, notification));
            return list;
        });
    }

    private String buildKey(NotificationDto not) {
        return not.getChatId() + "_" + not.getCoin().getName() + "_"
                + not.getMessageId() + "_" + not.getTargetPercentage() + "_"
                + not.getDirection();
    }

    private boolean isSameNotification(NotificationDto a, NotificationDto b) {
        return Objects.equals(a.getChatId(), b.getChatId()) &&
                Objects.equals(a.getCoin().getName(), b.getCoin().getName()) &&
                Objects.equals(a.getMessageId(), b.getMessageId()) &&
                Objects.equals(a.getTargetPercentage(), b.getTargetPercentage()) &&
                a.getDirection() == b.getDirection();
    }
}