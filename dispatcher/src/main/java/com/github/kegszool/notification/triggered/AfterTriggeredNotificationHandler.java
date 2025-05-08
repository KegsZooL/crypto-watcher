package com.github.kegszool.notification.triggered;

import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import com.github.kegszool.user.messaging.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AfterTriggeredNotificationHandler extends BaseResponseHandler<UserData> {

    private final String routingKey;
    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public AfterTriggeredNotificationHandler(
            @Value("${spring.rabbitmq.template.routing-key.get_user_data_after_triggered_notification_response}") String routingKey,
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
    public HandlerResult handle(ServiceMessage<UserData> serviceMessage) {
        menuUpdaterService.updateMenus(serviceMessage.getData(), serviceMessage.getChatId());
        return new HandlerResult.NoResponse();
    }
}
