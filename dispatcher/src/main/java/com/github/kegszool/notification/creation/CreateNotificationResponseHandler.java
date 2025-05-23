package com.github.kegszool.notification.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.menu.service.MenuUpdaterService;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

@Component
public class CreateNotificationResponseHandler extends BaseResponseHandler<UserData> {

    private final String routingKey;
    private final MenuUpdaterService menuUpdater;

    @Autowired
    public CreateNotificationResponseHandler(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_db}") String routingKey,
             MenuUpdaterService menuUpdater
    ) {
        this.routingKey = routingKey;
        this.menuUpdater = menuUpdater;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<UserData> serviceMessage) {
        menuUpdater.updateMenus(serviceMessage.getData(), serviceMessage.getChatId());
        return new HandlerResult.NoResponse();
    }
}