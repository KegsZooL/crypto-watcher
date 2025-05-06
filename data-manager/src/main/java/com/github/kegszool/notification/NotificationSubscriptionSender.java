package com.github.kegszool.notification;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;

@Component
public class NotificationSubscriptionSender {

    private final static Integer STUB_MESSAGE_ID = -1;
    private final static String STUB_CHAT_ID = "-1";

    private final String routingKey;
    private final ServiceMessageProducer msgProducer;

    @Autowired
    public NotificationSubscriptionSender(
            @Value("${spring.rabbitmq.template.routing-key.notification_websocket_subscription}") String routingKey,
            ServiceMessageProducer msgProducer
    ) {
        this.routingKey = routingKey;
        this.msgProducer = msgProducer;
    }

    public void send(List<NotificationDto> subscribed) {
        ServiceMessage<List<NotificationDto>> request = new ServiceMessage<>(
                STUB_MESSAGE_ID, STUB_CHAT_ID, subscribed
        );
        msgProducer.produce(request, routingKey);
    }
}