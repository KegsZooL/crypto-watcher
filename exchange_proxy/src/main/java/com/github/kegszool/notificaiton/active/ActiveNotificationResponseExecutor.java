package com.github.kegszool.notificaiton.active;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.executor.BaseRequestExecutor;
import com.github.kegszool.notificaiton.NotificationCacheService;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActiveNotificationResponseExecutor extends BaseRequestExecutor<List<NotificationDto>, NotificationDto> {

    private final NotificationCacheService activeNotificationStorage;

    @Autowired
    public ActiveNotificationResponseExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            NotificationCacheService notificationCacheService
    ) {
        super(restCryptoController, jsonParser);
        this.activeNotificationStorage = notificationCacheService;
    }

    @Override
    public ServiceMessage<NotificationDto> execute(ServiceMessage<List<NotificationDto>> serviceMessage) {
        List<NotificationDto> notifications = serviceMessage.getData();
        if (!notifications.isEmpty()) {
            String coinName = notifications.get(0).getCoin().getName();
            activeNotificationStorage.addNotifications(coinName, notifications);
        }
        return null;
    }

    @Override
    public String getResponseRoutingKey() {
        return "";
    }
}
