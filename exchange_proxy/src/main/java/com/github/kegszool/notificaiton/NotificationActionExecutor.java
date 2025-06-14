package com.github.kegszool.notificaiton;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.handler.NotificationHandler;

@Log4j2
@Component
public class NotificationActionExecutor {

    private final NotificationProducer producer;
    private final List<NotificationHandler> handlers;

    @Autowired
    public NotificationActionExecutor(
            NotificationProducer producer,
            List<NotificationHandler> handlers
    ) {
        this.producer = producer;
        this.handlers = handlers;
    }

    public void execute(NotificationDto notification, String coinName, double currentPrice, long now) {

        NotificationDto updated = new NotificationDto(
                notification.getUser(),
                notification.getMessageId(),
                notification.getChatId(),
                notification.getCoin(),
                notification.isRecurring(),
                true,
                notification.getInitialPrice(),
                currentPrice,
                notification.getTargetPercentage(),
                notification.getDirection(),
                now
        );
        producer.sendTriggeredNotification(updated);
        log.info("Notification triggered | Coin: {} | Chat ID: {} | Current price: {}",
                coinName, notification.getChatId(), currentPrice);

        handlers.stream()
                .filter(handler -> handler.supports(updated))
                .findFirst()
                .ifPresent(handler -> handler.handle(updated, coinName, currentPrice, now));
    }
}