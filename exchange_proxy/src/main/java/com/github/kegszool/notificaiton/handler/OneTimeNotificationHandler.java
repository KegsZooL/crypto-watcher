package com.github.kegszool.notificaiton.handler;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;
import com.github.kegszool.notificaiton.subscription.NotificationWebSocketSubscriber;

@Component
public class OneTimeNotificationHandler implements NotificationHandler {

    private final NotificationWebSocketSubscriber subscriber;
    private final TriggeredNotificationBuffer buffer;

    @Autowired
    public OneTimeNotificationHandler(
            @Lazy NotificationWebSocketSubscriber subscriber,
            TriggeredNotificationBuffer buffer
    ) {
        this.subscriber = subscriber;
        this.buffer = buffer;
    }

    @Override
    public boolean supports(NotificationDto notification) {
        return !notification.isRecurring();
    }

    @Override
    public void handle(NotificationDto notification, String coinName, double currentPrice) {
        subscriber.unsubscribe(notification);
        buffer.add(notification);
    }
}