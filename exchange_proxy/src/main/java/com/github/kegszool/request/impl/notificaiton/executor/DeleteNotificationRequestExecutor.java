package com.github.kegszool.request.impl.notificaiton.executor;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.websocket.SubscriptionTracker;
import com.github.kegszool.websocket.connection.OkxWebSocketConnector;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.request.BaseRequestExecutor;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import org.springframework.stereotype.Component;

@Component
public class DeleteNotificationRequestExecutor extends BaseRequestExecutor<NotificationDto, NotificationDto> {

    private final String routingKey;
    private final OkxWebSocketConnector webSocketConnector;
    private final SubscriptionTracker subscriptionTracker;

    public DeleteNotificationRequestExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            @Value("${spring.rabbitmq.template.routing-key.delete_notification_response}") String routingKey,
            OkxWebSocketConnector webSocketConnector,
            SubscriptionTracker subscriptionTracker
    ) {
        super(restCryptoController, jsonParser);
        this.routingKey = routingKey;
        this.webSocketConnector = webSocketConnector;
        this.subscriptionTracker = subscriptionTracker;
    }

    @Override
    public ServiceMessage<NotificationDto> execute(ServiceMessage<NotificationDto> serviceMessage) {


        NotificationDto notification = serviceMessage.getData();
        String coinName = notification.getCoin().getName();

        if (subscriptionTracker.decrement(coinName)) {
            webSocketConnector.disconnect(coinName);
        }
        return null;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}