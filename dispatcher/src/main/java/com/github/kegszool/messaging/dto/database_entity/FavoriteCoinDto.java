package com.github.kegszool.messaging.dto.database_entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCoinDto {
    private UserDto user;
    private CoinDto coin;
}