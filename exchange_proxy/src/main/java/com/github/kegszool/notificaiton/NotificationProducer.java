package com.github.kegszool.notificaiton;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;

@Service
public class NotificationProducer {

    private final ServiceMessageProducer producer;
    private final String routingKeyForCreation;
    private final String routingKeyForActive;
    private final String routingKeyForTriggered;
    private final String routingKeyAfterTriggered;
    private final String routingKeyForUpdating;

    @Autowired
    public NotificationProducer(
            ServiceMessageProducer producer,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request}") String routingKeyForCreation,
            @Value("${spring.rabbitmq.template.routing-key.get_activate_notification_request}") String routingKeyForActive,
            @Value("${spring.rabbitmq.template.routing-key.triggered_notification}") String routingKeyForTriggered,
            @Value("${spring.rabbitmq.template.routing-key.after_triggered_notification}") String routingKeyAfterTriggered,
            @Value("${spring.rabbitmq.template.routing-key.update_notification_request}") String routingKeyForUpdating
    ) {
        this.producer = producer;
        this.routingKeyForCreation = routingKeyForCreation;
        this.routingKeyForActive = routingKeyForActive;
        this.routingKeyForTriggered = routingKeyForTriggered;
        this.routingKeyAfterTriggered = routingKeyAfterTriggered;
        this.routingKeyForUpdating = routingKeyForUpdating;
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

    public void sendAfterTriggeredNotification(NotificationDto notification) {
        ServiceMessage<NotificationDto> request = new ServiceMessage<>(
                notification.getMessageId(),
                notification.getChatId().toString(),
                notification
        );
        producer.produce(request, routingKeyAfterTriggered);
    }

    public void sendUpdateNotificationRequest(List<NotificationDto> notifications) {
        Integer stubMessageId = notifications.get(0) .getMessageId();
        String stubChatId = notifications.get(0).getChatId().toString();
        ServiceMessage<List<NotificationDto>> request = new ServiceMessage<>(
                stubMessageId, stubChatId, notifications
        );
        producer.produce(request, routingKeyForUpdating);
    }
}