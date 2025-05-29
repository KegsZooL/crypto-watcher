package com.github.kegszool.notificaiton.handler;

import com.github.kegszool.messaging.dto.NotificationDto;

public interface NotificationHandler {
    boolean supports(NotificationDto notification);
    void handle(NotificationDto notification, String coinName, double currentPrice, long now);
}