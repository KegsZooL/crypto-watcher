package com.github.kegszool.request.impl;

import com.github.kegszool.messaging.dto.database_entity.*;
import com.github.kegszool.database.entity.service.impl.UserService;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.FavoriteCoin;

import com.github.kegszool.database.repository.impl.CoinRepository;
import com.github.kegszool.database.repository.impl.FavoriteCoinRepository;

import com.github.kegszool.request.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AddFavoriteCoinExecutor implements RequestExecutor<UserCoinData, UserData> {

    private final String routingKey;
    private final UserService userService;
    private final CoinRepository coinRepository;
    private final FavoriteCoinRepository favoriteCoinRepository;

    public AddFavoriteCoinExecutor(
            @Value("${spring.rabbitmq.template.routing-key.add_favorite_coin.response}") String routingKey,
            UserService userService,
            CoinRepository coinRepository,
            FavoriteCoinRepository favoriteCoinRepository
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
        this.coinRepository = coinRepository;
        this.favoriteCoinRepository = favoriteCoinRepository;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<UserCoinData> serviceMessage) {
        UserCoinData data = serviceMessage.getData();
        UserDto userDto = data.getUser();
        List<String> coinNamesToAdd = data.getCoinsToAdd();

        User user = userService.findOrCreateWithPreferences(userDto).getSecond();

        Map<String, Coin> allCoinsMap = getAllCoinsByName();
        List<Coin> coinsToAdd = getOrCreateCoins(coinNamesToAdd, allCoinsMap);
        coinRepository.saveAll(coinsToAdd);

        addFavoriteCoins(user, coinsToAdd);

        UserData userData = buildUserData(user, userDto);

        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    private Map<String, Coin> getAllCoinsByName() {
        return coinRepository.findAll().stream()
                .collect(Collectors.toMap(Coin::getName, Function.identity()));
    }

    private List<Coin> getOrCreateCoins(List<String> names, Map<String, Coin> existingCoins) {
        return names.stream()
                .map(name -> existingCoins.getOrDefault(name, new Coin(name)))
                .distinct()
                .toList();
    }

    private void addFavoriteCoins(User user, List<Coin> coins) {
        Set<String> existingFavorites = favoriteCoinRepository.findByUser_Id(user.getId()).stream()
                .map(fc -> fc.getCoin().getName())
                .collect(Collectors.toSet());

        List<FavoriteCoin> newFavorites = coins.stream()
                .filter(c -> !existingFavorites.contains(c.getName()))
                .map(c -> new FavoriteCoin(user, c))
                .toList();

        favoriteCoinRepository.saveAll(newFavorites);
    }

    private UserData buildUserData(User user, UserDto userDto) {
        int userId = user.getId();
        List<FavoriteCoinDto> favoriteCoins = userService.getUserFavoriteCoins(userId);
        String interfaceLanguage = userService.getInterfaceLanguage(userId);
        UserPreferenceDto userPreference = new UserPreferenceDto(userDto, interfaceLanguage);

        List<NotificationDto> notifications = userService.getUserNotifications(userId);
        return new UserData(userDto, favoriteCoins, notifications, userPreference);
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}