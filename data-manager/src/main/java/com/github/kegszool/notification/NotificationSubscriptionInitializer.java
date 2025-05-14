package com.github.kegszool.notification;

import java.util.List;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.database.entity.service.NotificationService;

@Service
public class NotificationSubscriptionInitializer {

    private final NotificationSubscriptionSender notificationSender;
    private final NotificationService notificationService;

    @Autowired
    public NotificationSubscriptionInitializer(
            NotificationSubscriptionSender notificationSender,
            NotificationService notificationService
    ) {
        this.notificationSender = notificationSender;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        List<NotificationDto> unTriggered = notificationService.getAllUntriggeredNotifications();
        if (!unTriggered.isEmpty()) {
        	notificationSender.send(unTriggered);
        }
    }
}