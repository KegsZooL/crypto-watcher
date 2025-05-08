package com.github.kegszool.notification.update;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

@Component
public class UpdatedNotificationSender {

    private final static Integer STUB_MESSAGE_ID_FOR_NOTIFICATION = -1;
    private final static String STUB_CHAT_ID_FOR_NOTIFICATION = "-1";

    private final String routingKey;
    private final ServiceMessageProducer msgProducer;

    @Autowired
    public UpdatedNotificationSender(@Value("${spring.rabbitmq.template.routing-key.update_notification.confirm}") String routingKey,
                                     ServiceMessageProducer msgProducer
    ) {
        this.routingKey = routingKey;
        this.msgProducer = msgProducer;
    }

    public void send(List<NotificationDto> updated) {
        ServiceMessage<List<NotificationDto>> serviceMsg = new ServiceMessage<>(
                STUB_MESSAGE_ID_FOR_NOTIFICATION, STUB_CHAT_ID_FOR_NOTIFICATION, updated
        );
        msgProducer.produce(serviceMsg, routingKey);
    }
}