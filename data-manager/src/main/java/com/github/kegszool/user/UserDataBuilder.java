package com.github.kegszool.user;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.mapper.impl.UserMapper;
import com.github.kegszool.database.entity.mapper.impl.FavoriteCoinMapper;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.entity.mapper.impl.UserPreferenceMapper;

import com.github.kegszool.messaging.dto.database_entity.*;
import com.github.kegszool.database.repository.impl.FavoriteCoinRepository;
import com.github.kegszool.database.repository.impl.NotificationRepository;
import com.github.kegszool.database.repository.impl.UserPreferenceRepository;

@Service
public class UserDataBuilder {

    private final UserMapper userMapper;
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    @Autowired
    public UserDataBuilder(
            UserMapper userMapper,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper,
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            UserPreferenceRepository userPreferenceRepository,
            UserPreferenceMapper userPreferenceMapper
    ) {
        this.userMapper = userMapper;
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    public UserData buildUserData(User user) {

        int userId = user.getId();
        UserDto userDto = userMapper.toDto(user);

        List<FavoriteCoinDto> favoriteCoins = favoriteCoinRepository.findByUser_Id(userId).stream()
                .map(favoriteCoinMapper::toDto)
                .toList();

        List<NotificationDto> notifications = notificationRepository.findByUser_IdAndIsTriggeredFalse(userId).stream()
                .map(notificationMapper::toDto)
                .toList();

        UserPreferenceDto preference = userPreferenceRepository.findById(userId)
                .map(userPreferenceMapper::toDto)
                .orElse(new UserPreferenceDto(userDto, "ru"));

        return new UserData(userDto, favoriteCoins, notifications, preference);
    }
}