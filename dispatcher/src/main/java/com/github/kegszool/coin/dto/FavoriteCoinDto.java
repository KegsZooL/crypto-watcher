package com.github.kegszool.coin.dto;

import com.github.kegszool.user.dto.UserDto;
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