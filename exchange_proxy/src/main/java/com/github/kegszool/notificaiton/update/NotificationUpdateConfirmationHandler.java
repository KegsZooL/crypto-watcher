package com.github.kegszool.notificaiton.update;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Component
public class NotificationUpdateConfirmationHandler {

    private final TriggeredNotificationBuffer triggeredBuffer;
    private final ActiveNotificationCacheService cacheService;

    @Autowired
    public NotificationUpdateConfirmationHandler(TriggeredNotificationBuffer triggeredBuffer,
                                                 ActiveNotificationCacheService cacheService) {
        this.triggeredBuffer = triggeredBuffer;
        this.cacheService = cacheService;
    }

    public void handle(List<NotificationDto> notifications) {
        notifications.forEach(not -> {
            triggeredBuffer.confirmUpdate(not);
            cacheService.confirmRemoval(not);
        });
    }
}