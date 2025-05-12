package com.github.kegszool.notificaiton.update;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Component
public class NotificationUpdateConfirmationHandler {

    private final ActiveNotificationCacheService cacheService;

    @Autowired
    public NotificationUpdateConfirmationHandler(
            ActiveNotificationCacheService cacheService
    ) {
        this.cacheService = cacheService;
    }

    public void handle(List<NotificationDto> notifications) {
        notifications.forEach(cacheService::confirmRemoval);
    }
}