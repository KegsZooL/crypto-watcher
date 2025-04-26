package com.github.kegszool.coin.dto;

import com.github.kegszool.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinData {
    private UserDto user;
    private List<String> coinsToAdd;
}