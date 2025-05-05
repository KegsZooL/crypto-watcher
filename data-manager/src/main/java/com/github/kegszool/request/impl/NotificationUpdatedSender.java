package com.github.kegszool.request.impl;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationUpdatedSender {

    private final static Integer STUB_MESSAGE_ID_FOR_NOTIFICATION = -1;
    private final static String STUB_CHAT_ID_FOR_NOTIFICATION = "-1";

    private final String routingKey;
    private final ResponseProducer responseProducer;

    @Autowired
    public NotificationUpdatedSender(@Value("${spring.rabbitmq.template.routing-key.update_notification.confirm}") String routingKey,
            ResponseProducer responseProducer
    ) {
        this.routingKey = routingKey;
        this.responseProducer = responseProducer;
    }

    public void send(List<NotificationDto> updated) {
        ServiceMessage<List<NotificationDto>> serviceMsg = new ServiceMessage<>(
                STUB_MESSAGE_ID_FOR_NOTIFICATION, STUB_CHAT_ID_FOR_NOTIFICATION, updated
        );
        responseProducer.produce(serviceMsg, routingKey);
    }
}