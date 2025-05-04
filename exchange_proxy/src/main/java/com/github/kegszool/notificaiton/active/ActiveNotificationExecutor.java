package com.github.kegszool.notificaiton.active;

import java.util.List;
import com.github.kegszool.utils.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.executor.BaseRequestExecutor;

@Component
public class ActiveNotificationExecutor extends BaseRequestExecutor<List<NotificationDto>, NotificationDto> {

    private final ActiveNotificationCacheService activeCacheService;

    @Autowired
    public ActiveNotificationExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            ActiveNotificationCacheService activeNotificationCacheService
    ) {
        super(restCryptoController, jsonParser);
        this.activeCacheService = activeNotificationCacheService;
    }

    @Override
    public ServiceMessage<NotificationDto> execute(ServiceMessage<List<NotificationDto>> serviceMessage) {
        List<NotificationDto> notifications = serviceMessage.getData();
        if (!notifications.isEmpty()) {
            String coinName = notifications.get(0).getCoin().getName();
            activeCacheService.add(coinName, notifications);
        }
        return null;
    }

    @Override
    public String getResponseRoutingKey() {
        return "";
    }
}