package com.github.kegszool.handler.impl;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.exception.EntityNotFoundException;
import com.github.kegszool.database.entity.service.impl.UserService;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class DeleteFavoriteCoinHandler extends BaseRequestHandler<UserData> {

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin.request}")
    private String DELETE_FAVORITE_COIN_REQUEST_TO_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin.response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_FROM_DATABASE_ROUTING_KEY;

    private final UserService userService;

    @Autowired
    public DeleteFavoriteCoinHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Class<UserData> getDataClass() {
        return UserData.class;
    }

    @Override
    public String getResponseRoutingKey() {
        return DELETE_FAVORITE_COIN_RESPONSE_FROM_DATABASE_ROUTING_KEY;
    }

    @Override
    public String getRequestRoutingKey() {
        return DELETE_FAVORITE_COIN_REQUEST_TO_DATABASE_ROUTING_KEY;
    }

    @Override
    public ServiceMessage<?> handle(ServiceMessage<UserData> serviceMessage) {
        UserData userData = serviceMessage.getData();
        UserDto user = userData.getUser();
        Long telegramId = user.getTelegramId();
                
        Optional<User> userOptional = userService.getUserByTelegramId(telegramId);
        if (userOptional.isPresent()) {
            return buildSuccessDeleteResponse(userData, userOptional.get(), serviceMessage);
        }
        throw createUserNotFoundException(telegramId);
    }

    private ServiceMessage<?> buildSuccessDeleteResponse(UserData userData, User user, ServiceMessage<UserData> request) {
        List<FavoriteCoinDto> coinsToRemove = userData.getFavoriteCoins();
        List<FavoriteCoinDto> remainingCoins = userService.deleteFavoriteCoins(user.getId(), coinsToRemove);
        userData.setFavoriteCoins(remainingCoins);
        return new ServiceMessage<>(request.getMessageId(), request.getChatId(), userData);
    }

    private EntityNotFoundException createUserNotFoundException(Long telegramId) {
        log.error("User not found with telegram id: {}", telegramId);
        return new EntityNotFoundException("User not found with telegram id: " + telegramId);
    }
}