package com.github.kegszool.notification.create;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;

@Component
public class CreatedNotificationSender {

    private final static Integer STUB_MESSAGE_ID_FOR_NOTIFICATION = -1;
    private final static String STUB_CHAT_ID_FOR_NOTIFICATION = "-1";

    private final String routingKey;
    private final ServiceMessageProducer msgProducer;

    @Autowired
    public CreatedNotificationSender(
            @Value("${spring.rabbitmq.template.routing-key.create_notification.notify}") String routingKey,
            ServiceMessageProducer msgProducer
    ) {
        this.msgProducer = msgProducer;
        this.routingKey = routingKey;
    }

    public void notify(String coinName) {
        ServiceMessage<String> notification = new ServiceMessage<>(
                STUB_MESSAGE_ID_FOR_NOTIFICATION, STUB_CHAT_ID_FOR_NOTIFICATION,
                coinName
        );
        msgProducer.produce(notification, routingKey);
    }
}