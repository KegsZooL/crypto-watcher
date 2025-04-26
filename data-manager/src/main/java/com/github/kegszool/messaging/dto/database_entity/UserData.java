package com.github.kegszool.messaging.dto.database_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private UserDto user;
    private List<FavoriteCoinDto> favoriteCoins;
    private List<NotificationDto> notifications;
}