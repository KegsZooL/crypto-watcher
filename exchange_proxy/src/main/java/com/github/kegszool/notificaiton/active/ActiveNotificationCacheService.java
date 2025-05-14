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

    public List<NotificationDto> getNotifications(String coinName) {
        return cache.getOrDefault(coinName, new CopyOnWriteArrayList<>());
    }

    public void add(String coinName, List<NotificationDto> notifications) {
        cache.compute(coinName, (k, existing) -> {
            if (existing == null) {
                log.info("Cache initialized for coin '{}': {} notifications added", coinName, notifications.size());
                return new CopyOnWriteArrayList<>(notifications);
            } else {
                int addedCount = 0;
                for (NotificationDto newNotification : notifications) {
                    boolean alreadyExists = existing.stream()
                            .anyMatch(existingNotification -> isSameNotification(existingNotification, newNotification));
                    if (!alreadyExists) {
                        existing.add(newNotification);
                        addedCount++;
                        log.info("New notification added: {}", newNotification);
                    } else {
                        log.info("Duplicate notification skipped: {}", newNotification);
                    }
                }
                log.info("For coin '{}': {} new notifications added, total now {}",
                        coinName, addedCount, existing.size());
                return existing;
            }
        });
    }

    public void remove(NotificationDto notification) {
        String coinName = notification.getCoin().getName();
        cache.computeIfPresent(coinName, (key, list) -> {
            list.removeIf(existing -> isSameNotification(existing, notification));
            return list;
        });
    }

    private boolean isSameNotification(NotificationDto a, NotificationDto b) {
        return Objects.equals(a.getChatId(), b.getChatId()) &&
                Objects.equals(a.getCoin().getName(), b.getCoin().getName()) &&
                Objects.equals(a.getMessageId(), b.getMessageId()) &&
                Objects.equals(a.getTargetPercentage(), b.getTargetPercentage()) &&
                a.getDirection() == b.getDirection();
    }
}