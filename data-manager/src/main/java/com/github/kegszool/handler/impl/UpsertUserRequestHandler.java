package com.github.kegszool.handler.impl;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.service.impl.UserService;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class UpsertUserRequestHandler extends BaseRequestHandler<UserDto> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user.request}")
    private String UPSERT_USER_REQUEST_TO_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.upsert_user.response}")
    private String UPSERT_USER_RESPONSE_FROM_DATABASE_ROUTING_KEY;

    private final UserService userService;

    @Autowired
    public UpsertUserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getRequestRoutingKey() {
        return UPSERT_USER_REQUEST_TO_DATABASE_ROUTING_KEY;
    }

    @Override
    public String getResponseRoutingKey() {
        return UPSERT_USER_RESPONSE_FROM_DATABASE_ROUTING_KEY;
    }

    @Override
    public Class<UserDto> getDataClass() {
        return UserDto.class;
    }

    @Override
    public ServiceMessage<?> handle(ServiceMessage<UserDto> serviceMessage) {
        UserDto userDto = serviceMessage.getData();
        Pair<Boolean, User> result = upsertUser(userDto);
        return createResponseServiceMessage(serviceMessage, result, userDto);
    }

    private Pair<Boolean, User> upsertUser(UserDto userDto) {
        Long telegramId = userDto.getTelegramId();
        return userService.getUserByTelegramId(telegramId)
                .map(user -> Pair.of(true, user))
                .orElseGet(() -> {
                    logNonExistentUser(userDto, telegramId);
                    return Pair.of(false, userService.saveEntity(userDto));
                });
    }

    private void logNonExistentUser(UserDto userDto, Long telegramId) {
        log.info("The user=[telgeram_id= \"%s\", first_name=\"%s\", last_name=\"%s\"]" +
                " does not exist in the database",
                telegramId, userDto.getFirstName(), userDto.getLastName());
    }

    private ServiceMessage<UpsertUserResponse> createResponseServiceMessage(
            ServiceMessage<?> requestServiceMessage,
            Pair<Boolean, User> upsertUserResult,
            UserDto userDto
    ) {
        Boolean userExistBefore = upsertUserResult.getFirst();
        User user = upsertUserResult.getSecond();

        int userId = user.getId();
        Pair<List<FavoriteCoinDto>, List<NotificationDto>> userAdditionalData = fetchUserAdditionalData(userId);

        UpsertUserResponse response = new UpsertUserResponse(
                userExistBefore, userDto, userAdditionalData.getFirst(), userAdditionalData.getSecond()
        );
        return new ServiceMessage<>(requestServiceMessage.getMessageId(), requestServiceMessage.getChatId(), response);
    }

    private Pair<List<FavoriteCoinDto>, List<NotificationDto>> fetchUserAdditionalData(int userId) {
        return Pair.of(userService.getUserFavoriteCoins(userId), userService.getUserNotifications(userId));
    }
}