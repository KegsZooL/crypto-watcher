package com.github.kegszool.notificaiton;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;

@Component
public class TriggeredNotificationBuffer {

    private final ReentrantLock lock = new ReentrantLock();

    private final ConcurrentMap<String, Queue<NotificationDto>> buffer = new ConcurrentHashMap<>();

    public void add(NotificationDto notification) {
        String key = buildKey(notification);
        buffer.compute(key, (k, existingQueue) -> {
            if (existingQueue == null) {
                existingQueue = new ConcurrentLinkedQueue<>();
            }
            existingQueue.add(notification);
            return existingQueue;
        });
    }

    public List<NotificationDto> drain() {
        lock.lock();
        try {
            List<NotificationDto> all = new ArrayList<>();
            for (Queue<NotificationDto> queue : buffer.values()) {
                all.addAll(queue);
            }
            buffer.clear();
            return all;
        } finally {
            lock.unlock();
        }
    }

    private String buildKey(NotificationDto not) {
        return not.getChatId() + "_" + not.getCoin().getName() + "_"
                + not.getMessageId() + "_" + not.getTargetPercentage() + "_"
                + not.getDirection();
    }
}