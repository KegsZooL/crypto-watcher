package com.github.kegszool.notification.messaging;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.notification.messaging.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.kegszool.messaging.producer.RequestProducerService;

import javax.management.Notification;

@Component
public class CreateNotificationRequestSender {

    private final String routingKey;
    private final RequestProducerService requestProducer;

    @Autowired
    public CreateNotificationRequestSender(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request}") String routingKey,
            RequestProducerService requestProducer
    ) {
        this.routingKey = routingKey;
        this.requestProducer = requestProducer;
    }

    public void send(NotificationDto notificationDto) {
        ServiceMessage<Notification> request = new ServiceMessage(

        );
        requestProducer.produce(routingKey, request);
    }
}