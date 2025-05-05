package com.github.kegszool.notificaiton.util;

import java.util.List;

import com.github.kegszool.notificaiton.NotificationProducer;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
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
//        resendUnconfirmedUpdates();
    }

//    private void resendUnconfirmedUpdates() {
//        List<NotificationDto> unconfirmed = buffer.getPendingConfirmations();
//        if (!unconfirmed.isEmpty()) {
//            log.warn("Resending {} unconfirmed updates", unconfirmed.size());
//            producer.sendUpdateNotificationRequest(unconfirmed);
//        }
//    }
}