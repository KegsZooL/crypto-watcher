package com.github.kegszool.notification.creation.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class CreateNotificationRequestSender {

    private final String routingKey;
    private final RequestProducerService requestProducer;

    @Autowired
    public CreateNotificationRequestSender(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_exchange}") String routingKey,
            RequestProducerService requestProducer
    ) {
        this.routingKey = routingKey;
        this.requestProducer = requestProducer;
    }

    public void send(NotificationDto notification, Integer msgId, String chatId) {
        ServiceMessage<NotificationDto> request = new ServiceMessage<>(
                msgId,
                chatId,
                notification
        );
        requestProducer.produce(routingKey, request);
    }
}