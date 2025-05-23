package com.github.kegszool.coin.delete;

import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.exception.EntityNotFoundException;
import com.github.kegszool.database.entity.service.UserService;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;

@Log4j2
@Service
public class DeleteFavoriteCoinExecutor implements RequestExecutor<UserData, UserData> {

    private final String routingKey;
    private final UserService userService;

    @Autowired
    public DeleteFavoriteCoinExecutor(
            @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin.response}") String routingKey,
            UserService userService
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<UserData> serviceMessage) {
        UserData userData = serviceMessage.getData();
        UserDto user = userData.getUser();
        Long telegramId = user.getTelegramId();
                
        Optional<User> userOptional = userService.getUserByTelegramId(telegramId);
        if (userOptional.isPresent()) {
            return buildSuccessDeleteResponse(userData, userOptional.get(), serviceMessage);
        }
        throw createUserNotFoundException(telegramId);
    }

    private ServiceMessage<UserData> buildSuccessDeleteResponse(UserData userData, User user, ServiceMessage<UserData> request) {
        List<FavoriteCoinDto> coinsToRemove = userData.getFavoriteCoins();
        List<FavoriteCoinDto> remainingCoins = userService.deleteFavoriteCoins(user.getId(), coinsToRemove);
        userData.setFavoriteCoins(remainingCoins);
        userData.setNotifications(userService.getUserNotifications(user.getId()));
        return new ServiceMessage<>(request.getMessageId(), request.getChatId(), userData);
    }

    private EntityNotFoundException createUserNotFoundException(Long telegramId) {
        log.error("User not found with telegram id: {}", telegramId);
        return new EntityNotFoundException("User not found with telegram id: " + telegramId);
    }
}