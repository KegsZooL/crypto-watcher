package com.github.kegszool.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.notification.NotificationDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private UserDto user;
    private List<FavoriteCoinDto> favoriteCoins;
    private List<NotificationDto> notifications;
}