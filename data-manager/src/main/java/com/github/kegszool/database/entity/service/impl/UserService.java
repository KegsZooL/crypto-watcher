package com.github.kegszool.database.entity.service.impl;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.entity.base.Notification;

import com.github.kegszool.database.entity.mapper.impl.UserMapper;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.entity.mapper.impl.FavoriteCoinMapper;

import com.github.kegszool.database.entity.service.EntityService;

import com.github.kegszool.database.repository.impl.UserRepository;
import com.github.kegszool.database.repository.impl.FavoriteCoinRepository;
import com.github.kegszool.database.repository.impl.NotificationRepository;

import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.database_entity.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends EntityService<User, UserDto, Integer> {

    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper,
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper
    ) {
        super(userRepository, userMapper);
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
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
}