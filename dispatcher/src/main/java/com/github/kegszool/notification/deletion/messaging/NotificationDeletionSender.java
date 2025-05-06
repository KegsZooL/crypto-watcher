package com.github.kegszool.notification.deletion.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducer;

@Component
public class NotificationDeletionSender {

    private final String routingKey;
    private final RequestProducer requestProducer;

    @Autowired
    public NotificationDeletionSender(
            @Value("${spring.rabbitmq.template.routing-key.delete_notification_request}") String routingKey,
            RequestProducer requestProducer
    ) {
        this.routingKey = routingKey;
        this.requestProducer = requestProducer;
    }

    public void send(Integer messageId, String chatId, NotificationIdentifierDto notification) {
        ServiceMessage<NotificationIdentifierDto> request = new ServiceMessage<>(
                messageId, chatId, notification
        );
        requestProducer.produce(routingKey, request);
    }
}