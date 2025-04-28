package com.github.kegszool.request_executor.impl;

import com.github.kegszool.messaging.dto.database_entity.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.service.impl.UserService;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;

import com.github.kegszool.request_executor.RequestExecutor;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;

@Log4j2
@Component
public class UpsertUserRequestExecutor implements RequestExecutor<UserDto, UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user.response}")
    private String upsertUserResponseRoutingKey;

    private final UserService userService;

    @Autowired
    public UpsertUserRequestExecutor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getResponseRoutingKey() {
        return upsertUserResponseRoutingKey;
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