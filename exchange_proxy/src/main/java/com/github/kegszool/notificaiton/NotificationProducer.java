package com.github.kegszool.notificaiton;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.producer.ServiceMessageProducer;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import java.util.List;

@Service
public class NotificationProducer {

    private final ServiceMessageProducer producer;
    private final String routingKeyForCreation;
    private final String routingKeyForActive;
    private final String routingKeyForTriggered;

    @Autowired
    public NotificationProducer(
            ServiceMessageProducer producer,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request}") String routingKeyForCreation,
            @Value("${spring.rabbitmq.template.routing-key.get_activate_notification_request}") String routingKeyForActive,
            @Value("${spring.rabbitmq.template.routing-key.triggered_notification}") String routingKeyForTriggered
    ) {
        this.producer = producer;
        this.routingKeyForCreation = routingKeyForCreation;
        this.routingKeyForActive = routingKeyForActive;
        this.routingKeyForTriggered = routingKeyForTriggered;
    }

    public void sendCreationRequest(ServiceMessage<NotificationDto> serviceMessage) {
        producer.produce(serviceMessage, routingKeyForCreation);
    }

    public void sendUploadRequestForActive(String coinName, ServiceMessage<?> serviceMessage) {
        ServiceMessage<String> request = new ServiceMessage<>(
                serviceMessage.getMessageId(),
                serviceMessage.getChatId(),
                coinName
        );
        producer.produce(request, routingKeyForActive);
    }

    public void sendTriggeredNotification(NotificationDto notification) {
        ServiceMessage<NotificationDto> request = new ServiceMessage<>(
                notification.getMessageId(),
                notification.getChatId().toString(),
                notification
        );
        producer.produce(request, routingKeyForTriggered);
    }

    public void sendUpdateNotificationRequest(List<NotificationDto> notifications) {
//        Integer stubMessageId = notifications.get(0) .getMessageId();
//        String stubChatId = notifications.get(0).getChatId().toString();
//        ServiceMessage<List<NotificationDto>> request = new ServiceMessage<>(
//                stubMessageId, stubChatId, notifications
//        );
//        producer.produce(request,);
    }
}