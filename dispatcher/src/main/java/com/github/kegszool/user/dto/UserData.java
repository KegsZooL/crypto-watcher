package com.github.kegszool.user.dto;

import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.notification.NotificationDto;
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