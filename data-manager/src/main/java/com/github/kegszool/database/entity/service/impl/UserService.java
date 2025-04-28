package com.github.kegszool.database.entity.service.impl;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.*;
import com.github.kegszool.database.entity.service.EntityService;
import com.github.kegszool.database.entity.mapper.impl.UserMapper;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.entity.mapper.impl.FavoriteCoinMapper;

import org.springframework.data.util.Pair;
import com.github.kegszool.database.repository.impl.*;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;


@Service
public class UserService extends EntityService<User, UserDto, Integer> {

    private final static List<String> DEFAULT_COIN_NAMES = List.of(
            "BTC", "ETH", "DOGE",
            "USDT", "XRP", "SOL",
            "AVAX", "LINK"
    );

    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final CoinRepository coinRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserPreferenceRepository userPreferenceRepository,
            UserMapper userMapper,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper,
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            CoinRepository coinRepository
    ) {
        super(userRepository, userMapper);
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.coinRepository = coinRepository;
    }

    public Optional<User> getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public List<FavoriteCoinDto> getUserFavoriteCoins(int id) {
        List<FavoriteCoin> favoriteCoins = favoriteCoinRepository.findByUser_Id(id);
        return favoriteCoins.stream()
                .map(favoriteCoinMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> getUserNotifications(int id) {
        List<Notification> notifications = notificationRepository.findByUser_Id(id);
        return notifications.stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FavoriteCoinDto> deleteFavoriteCoins(int id, List<FavoriteCoinDto> removableCoins) {
	    List<FavoriteCoin> existingCoins = favoriteCoinRepository.findByUser_Id(id);
        List<String> coinsToRemove = removableCoins.stream()
                .map(coin -> coin.getCoin().getName())
                .toList();

        existingCoins.stream()
                .filter(coin -> coinsToRemove.contains(coin.getCoin().getName()))
                .forEach(favoriteCoinRepository::delete);

        return existingCoins.stream()
                .filter(coin -> !coinsToRemove.contains(coin.getCoin().getName()))
                .map(favoriteCoinMapper::toDto)
                .toList();
    }

    public String getInterfaceLanguage(int userId) {
        return userPreferenceRepository.findById(userId)
                .map(UserPreference::getInterfaceLanguage)
                .orElse("ru");
    }

    public Pair<Boolean, User> findOrCreateWithPreferences(UserDto userDto) {
        return userRepository.findByTelegramId(userDto.getTelegramId())
                .map(user -> Pair.of(true, user))
                .orElseGet(() -> {
                    User newUser = new User(userDto.getTelegramId(), userDto.getFirstName(), userDto.getLastName());
                    User savedUser = userRepository.save(newUser);

                    UserPreference preference = new UserPreference(savedUser, "ru");
                    userPreferenceRepository.save(preference);

                    addDefaultFavoriteCoins(savedUser);

                    return Pair.of(false, savedUser);
                });
    }

    private void addDefaultFavoriteCoins(User user) {

        List<Coin> existingCoins = coinRepository.findByNameIn(DEFAULT_COIN_NAMES);
        Map<String, Coin> existingCoinMap = existingCoins.stream()
                .collect(Collectors.toMap(Coin::getName, Function.identity()));

        List<Coin> coinsToSave = new ArrayList<>();

        for (String coinName : DEFAULT_COIN_NAMES) {
            if (!existingCoinMap.containsKey(coinName)) {
                Coin newCoin = new Coin(coinName);
                coinsToSave.add(newCoin);
                existingCoinMap.put(coinName, newCoin);
            }
        }

        if (!coinsToSave.isEmpty()) {
            coinRepository.saveAll(coinsToSave);
        }

        List<FavoriteCoin> favoriteCoins = DEFAULT_COIN_NAMES.stream()
                .map(name -> new FavoriteCoin(user, existingCoinMap.get(name)))
                .collect(Collectors.toList());

        favoriteCoinRepository.saveAll(favoriteCoins);
    }
}