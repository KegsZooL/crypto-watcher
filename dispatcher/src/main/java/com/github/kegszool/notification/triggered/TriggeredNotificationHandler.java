package com.github.kegszool.notification.triggered;

import com.github.kegszool.notification.messaging.dto.NotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

@Component
public class TriggeredNotificationHandler extends BaseResponseHandler<NotificationDto> {

    private final String routingKey;
    private final TriggeredNotificationMessageBuilder messageBuilder;

    @Autowired
    public TriggeredNotificationHandler(
            @Value("${spring.rabbitmq.template.routing-key.triggered_notification}") String routingKey,
            TriggeredNotificationMessageBuilder messageBuilder
    ) {
        this.routingKey = routingKey;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<NotificationDto> serviceMessage) {
        NotificationDto notification = serviceMessage.getData();
        return new HandlerResult.Success(messageBuilder.build(notification));
    }
}