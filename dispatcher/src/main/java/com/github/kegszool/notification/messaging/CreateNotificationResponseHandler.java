package com.github.kegszool.notification.messaging;

import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import com.github.kegszool.user.messaging.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        menuUpdater.updateMenus(serviceMessage.getData());
        return new HandlerResult.NoResponse();
    }
}