package com.github.kegszool.request_executor.impl;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.entity.mapper.impl.FavoriteCoinMapper;

import com.github.kegszool.database.repository.impl.UserRepository;
import com.github.kegszool.database.repository.impl.CoinRepository;
import com.github.kegszool.database.repository.impl.FavoriteCoinRepository;

import com.github.kegszool.request_executor.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AddFavoriteCoinExecutor implements RequestExecutor<UserCoinData, UserData> {

    private final String routingKey;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;

    public AddFavoriteCoinExecutor(
            @Value("${spring.rabbitmq.template.routing-key.add_favorite_coin.response}")
            String routingKey,
            UserRepository userRepository,
            CoinRepository coinRepository,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper
    ) {
        this.routingKey = routingKey;
        this.userRepository = userRepository;
        this.coinRepository = coinRepository;
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<UserCoinData> serviceMessage) {

        UserCoinData data = serviceMessage.getData();
        UserDto userDto = data.getUser();
        List<String> coinNamesToAdd = data.getCoinsToAdd();

        User user = findOrCreateUser(userDto);
        Map<String, Coin> allCoinsMap = getAllCoinsByName();

        List<Coin> coinsToAdd = getOrCreateCoins(coinNamesToAdd, allCoinsMap);
        coinRepository.saveAll(coinsToAdd);

        addFavoriteCoins(user, coinsToAdd);

        List<FavoriteCoin> userFavorites = favoriteCoinRepository.findByUser_Id(user.getId());
        List<FavoriteCoinDto> favoriteCoinDtos = userFavorites.stream()
                .map(favoriteCoinMapper::toDto)
                .toList();

        //TODO: notification empty
        UserData userData = new UserData(userDto, favoriteCoinDtos, Collections.emptyList());

        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    private User findOrCreateUser(UserDto user) {
        return userRepository.findByTelegramId(user.getTelegramId())
                .orElseGet(() -> userRepository.save(
                        new User(user.getTelegramId(), user.getFirstName(), user.getLastName())
                ));
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
        Set<String> existingFavorites = favoriteCoinRepository.findByUser_Id(user.getId())
                .stream()
                .map(fc -> fc.getCoin().getName())
                .collect(Collectors.toSet());

        List<FavoriteCoin> newFavorites = coins.stream()
                .filter(c -> !existingFavorites.contains(c.getName()))
                .map(c -> new FavoriteCoin(user, c))
                .toList();

        favoriteCoinRepository.saveAll(newFavorites);
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}