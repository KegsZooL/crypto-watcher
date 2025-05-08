package com.github.kegszool.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.service.impl.UserService;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.database_entity.*;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.notification.NotificationSubscriptionSender;

@Log4j2
@Service
public class UpsertUserExecutor implements RequestExecutor<UserDto, UpsertUserResponse> {

    private final String routingKey;
    private final UserService userService;
    private final NotificationSubscriptionSender notificationSender;
    private final UserDataBuilder userDataBuilder;

    @Autowired
    public UpsertUserExecutor(
            @Value("${spring.rabbitmq.template.routing-key.upsert_user.response}") String routingKey,
            UserService userService,
            NotificationSubscriptionSender notificationSender,
            UserDataBuilder userDataBuilder
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
        this.notificationSender = notificationSender;
        this.userDataBuilder = userDataBuilder;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }

    @Override
    public ServiceMessage<UpsertUserResponse> execute(ServiceMessage<UserDto> serviceMessage) {
        UserDto userDto = serviceMessage.getData();
        Pair<Boolean, User> upsertResult = userService.findOrCreate(userDto);
        return buildResponse(serviceMessage, upsertResult);
    }

    private ServiceMessage<UpsertUserResponse> buildResponse(ServiceMessage<UserDto> request,  Pair<Boolean, User> upsertResult) {
        Boolean userExistedBefore = upsertResult.getFirst();
        User user = upsertResult.getSecond();

        UserData userData = userDataBuilder.buildUserData(user);
        UpsertUserResponse response = new UpsertUserResponse(userExistedBefore, userData);
        notificationSender.send(userData.getNotifications());

        return new ServiceMessage<>(request.getMessageId(), request.getChatId(), response);
    }
}