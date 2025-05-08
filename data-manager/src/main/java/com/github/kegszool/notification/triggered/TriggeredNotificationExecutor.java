package com.github.kegszool.notification.triggered;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.service.impl.UserService;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserData;

import com.github.kegszool.user.UserDataBuilder;
import com.github.kegszool.user.UserNotFoundException;

@Log4j2
@Component
public class TriggeredNotificationExecutor implements RequestExecutor<NotificationDto, UserData> {

    private final String routingKey;
    private final UserDataBuilder userDataBuilder;
    private final UserService userService;

    @Autowired
    public TriggeredNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.after_triggered_notification.response}") String routingKey,
            UserDataBuilder userDataBuilder,
            UserService userService
    ) {
        this.routingKey = routingKey;
        this.userDataBuilder = userDataBuilder;
        this.userService = userService;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<NotificationDto> serviceMessage) {
        NotificationDto notification = serviceMessage.getData();
        Long telegramId = notification.getUser().getTelegramId();
        User user = userService.getUserByTelegramId(telegramId)
                .orElseThrow(() -> {
                    log.error("User with telegram ID {} not found", telegramId);
                    return new UserNotFoundException(telegramId);
                });
        UserData userData = userDataBuilder.buildUserDataWithoutNotification(user, notification);
        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}