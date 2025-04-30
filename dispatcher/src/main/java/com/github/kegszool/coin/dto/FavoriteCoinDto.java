package com.github.kegszool.coin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.github.kegszool.user.messaging.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCoinDto {
    private UserDto user;
    private CoinDto coin;
}