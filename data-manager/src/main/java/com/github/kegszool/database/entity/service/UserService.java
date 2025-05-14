package com.github.kegszool.database.entity.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.*;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.entity.mapper.impl.FavoriteCoinMapper;

import org.springframework.data.util.Pair;
import com.github.kegszool.database.repository.impl.*;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

@Service
public class UserService{

    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserPreferenceRepository userPreferenceRepository,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper,
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper
    ) {
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
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
        List<Notification> notifications = notificationRepository.findByUser_IdAndIsTriggeredFalse(id);
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

    public Pair<Boolean, User> findOrCreate(UserDto userDto) {
        return userRepository.findByTelegramId(userDto.getTelegramId())
                .map(user -> Pair.of(true, user))
                .orElseGet(() -> {
                    User newUser = new User(userDto.getTelegramId(), userDto.getFirstName(), userDto.getLastName());
                    User savedUser = userRepository.save(newUser);

                    UserPreference preference = new UserPreference(savedUser, "ru");
                    userPreferenceRepository.save(preference);

                    return Pair.of(false, savedUser);
                });
    }
}