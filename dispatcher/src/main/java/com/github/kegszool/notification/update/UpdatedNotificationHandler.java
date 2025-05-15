package com.github.kegszool.notification.update;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.service.MenuUpdaterService;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class UpdatedNotificationHandler extends BaseResponseHandler<List<UserNotificationUpdateDto>> {

    private final String routingKey;
    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public UpdatedNotificationHandler(
            @Value("${spring.rabbitmq.template.routing-key.update_notification_response}") String routingKey,
            MenuUpdaterService menuUpdaterService
    ) {
        this.routingKey = routingKey;
        this.menuUpdaterService = menuUpdaterService;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<List<UserNotificationUpdateDto>> serviceMessage) {
        serviceMessage.getData().forEach(not ->
                menuUpdaterService.updateMenus(not.getUserData(), not.getChatId()));
        return new HandlerResult.NoResponse();
    }
}