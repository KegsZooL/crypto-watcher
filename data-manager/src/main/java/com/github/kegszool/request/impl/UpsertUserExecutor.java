package com.github.kegszool.request.impl;

import com.github.kegszool.notification.NotificationSubscriptionSender;
import com.github.kegszool.messaging.dto.database_entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.service.impl.UserService;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;

import com.github.kegszool.request.RequestExecutor;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UpsertUserExecutor implements RequestExecutor<UserDto, UpsertUserResponse> {

    private final String routingKey;
    private final UserService userService;
    private final NotificationSubscriptionSender notificationSender;

    @Autowired
    public UpsertUserExecutor(
            @Value("${spring.rabbitmq.template.routing-key.upsert_user.response}") String routingKey,
            UserService userService,
            NotificationSubscriptionSender notificationSender
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
        this.notificationSender = notificationSender;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }

    @Override
    public ServiceMessage<UpsertUserResponse> execute(ServiceMessage<UserDto> serviceMessage) {
        UserDto userDto = serviceMessage.getData();
        Pair<Boolean, User> upsertResult = userService.findOrCreateWithPreferences(userDto);
        return buildResponse(serviceMessage, upsertResult, userDto);
    }

    private ServiceMessage<UpsertUserResponse> buildResponse(
            ServiceMessage<UserDto> request,
            Pair<Boolean, User> upsertResult,
            UserDto userDto
    ) {
        Boolean userExistedBefore = upsertResult.getFirst();
        User user = upsertResult.getSecond();

        UserData userData = loadUserData(user, userDto);
        UpsertUserResponse response = new UpsertUserResponse(userExistedBefore, userData);

        notificationSender.send(userData.getNotifications());
        return new ServiceMessage<>(request.getMessageId(), request.getChatId(), response);
    }

    private UserData loadUserData(User user, UserDto userDto) {
        int userId = user.getId();
        List<FavoriteCoinDto> favoriteCoins = userService.getUserFavoriteCoins(userId);
        List<NotificationDto> notifications = userService.getUserNotifications(userId);
        String interfaceLanguage = userService.getInterfaceLanguage(userId);

        UserPreferenceDto userPreference = new UserPreferenceDto(userDto, interfaceLanguage);
        return new UserData(userDto, favoriteCoins, notifications, userPreference);
    }
}