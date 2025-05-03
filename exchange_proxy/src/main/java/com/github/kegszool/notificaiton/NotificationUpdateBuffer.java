package com.github.kegszool.notificaiton;

import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationUpdateBuffer {

    private final Map<String, NotificationDto> buffer = new ConcurrentHashMap<>();

    public void add(NotificationDto notification) {
        String key = buildKey(notification);
        buffer.put(key, notification);
    }

    public List<NotificationDto> drain() {
        List<NotificationDto> values = new ArrayList<>(buffer.values());
        buffer.clear();
        return values;
    }

    private String buildKey(NotificationDto not) {
        return not.getChatId() + "_" + not.getCoin().getName() + "_" + not.getMessageId();
    }
}