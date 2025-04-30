package com.github.kegszool.coin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import com.github.kegszool.user.messaging.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinData {
    private UserDto user;
    private List<String> coinsToAdd;
}