package com.github.kegszool.notificaiton.deletion;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.messaging.executor.BaseRequestExecutor;
import com.github.kegszool.websocket.util.WebSocketSubscriptionTracker;
import com.github.kegszool.websocket.connection.OkxWebSocketConnector;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import org.springframework.stereotype.Component;

@Component
public class DeleteNotificationRequestExecutor extends BaseRequestExecutor<NotificationDto, NotificationDto> {

    private final String routingKey;
    private final OkxWebSocketConnector webSocketConnector;
    private final WebSocketSubscriptionTracker subscriptionTracker;

    public DeleteNotificationRequestExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            @Value("${spring.rabbitmq.template.routing-key.delete_notification_response}") String routingKey,
            OkxWebSocketConnector webSocketConnector,
            WebSocketSubscriptionTracker subscriptionTracker
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