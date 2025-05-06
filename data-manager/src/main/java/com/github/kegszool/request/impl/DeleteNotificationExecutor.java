package com.github.kegszool.request.impl;

import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.notification.NotificationIdentifierDto;
import com.github.kegszool.request.RequestExecutor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DeleteNotificationExecutor implements RequestExecutor<NotificationIdentifierDto, UserData> {

    private final String routingKey;

    @Autowired
    public DeleteNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.delete_notification.response}") String routingKey
    ) {
        this.routingKey = routingKey;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<NotificationIdentifierDto> serviceMessage) {
        return null;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}