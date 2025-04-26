package com.github.kegszool.messaging.dto.database_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCoinDto {
    private UserDto user;
    private CoinDto coin;
}