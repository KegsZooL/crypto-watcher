package com.github.kegszool.notificaiton.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.messaging.executor.BaseRequestExecutor;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class NotificationCreationExecutor extends BaseRequestExecutor<NotificationDto, NotificationCreationStatus> {

    private final String routingKey;
    private final NotificationCreationService creationService;

    @Autowired
    public NotificationCreationExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response}") String routingKey,
            NotificationCreationService creationService
    ) {
        super(restCryptoController, jsonParser);
        this.routingKey =routingKey;
        this.creationService = creationService;
    }

    @Override
    public ServiceMessage<NotificationCreationStatus> execute(ServiceMessage<NotificationDto> serviceMessage) {

        boolean creationStatus = creationService.processCreationRequest(serviceMessage);
        NotificationCreationStatus notificationCreationStatus = new NotificationCreationStatus(
                creationStatus, serviceMessage.getData().getCoin().getName());

        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), notificationCreationStatus);
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}