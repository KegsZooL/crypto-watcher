package com.github.kegszool.user;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.user.messaging.dto.UserPreferenceDto;

import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.localization.LocalizationService;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class UserDataFactory {

    private final LocalizationService localizationService;

    @Autowired
    public UserDataFactory(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public UserData create(UserDto user, List<FavoriteCoinDto> favorites, List<NotificationDto> notifications, String chatId) {
        UserPreferenceDto preference = new UserPreferenceDto(user, localizationService.getLocale(chatId));
        return new UserData(user, favorites, notifications, preference);
    }
}