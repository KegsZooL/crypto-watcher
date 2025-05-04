package com.github.kegszool.notificaiton.util;

import java.util.List;

import com.github.kegszool.notificaiton.NotificationProducer;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;
import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class NotificationBufferFlusher {

    private final TriggeredNotificationBuffer buffer;
    private final NotificationProducer producer;

    @Autowired
    public NotificationBufferFlusher (TriggeredNotificationBuffer buffer, NotificationProducer producer) {
        this.buffer = buffer;
        this.producer = producer;
    }

    @Scheduled(fixedDelay = 1000)
    public void flush() {
        List<NotificationDto> updates = buffer.drain();
        if (!updates.isEmpty()) {
            producer.sendUpdateNotificationRequest(updates);
        }
    }
}