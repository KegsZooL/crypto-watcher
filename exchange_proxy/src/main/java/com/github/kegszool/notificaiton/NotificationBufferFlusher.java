package com.github.kegszool.notificaiton;

import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Component
public class NotificationBufferFlusher {

    private final NotificationUpdateBuffer buffer;
    private final NotificationProducer producer;

    @Autowired
    public NotificationBufferFlusher (NotificationUpdateBuffer buffer, NotificationProducer producer) {
        this.buffer = buffer;
        this.producer = producer;
    }

//    @Scheduled(fixedDelay = 5000)
    public void flush() {
        List<NotificationDto> updates = buffer.drain();
        if (!updates.isEmpty()) {
            producer.sendUpdateNotificationRequest(updates);
        }
    }
}
