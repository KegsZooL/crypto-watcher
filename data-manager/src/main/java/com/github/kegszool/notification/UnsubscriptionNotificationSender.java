package com.github.kegszool.notification;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;

@Component
public class UnsubscriptionNotificationSender {

    private final String routingKey;
    private final ServiceMessageProducer messageProducer;

    @Autowired
    public UnsubscriptionNotificationSender(
            @Value("${spring.rabbitmq.template.routing-key.notification_websocket_unsubscription}") String routingKey,
            ServiceMessageProducer messageProducer
    ) {
        this.routingKey = routingKey;
        this.messageProducer = messageProducer;
    }

    public void send(NotificationDto deleted, Integer msgId, String chatId) {
        ServiceMessage<NotificationDto> request = new ServiceMessage<>(msgId, chatId, deleted);
        messageProducer.produce(request, routingKey);
    }
}