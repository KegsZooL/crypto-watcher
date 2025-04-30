package com.github.kegszool.coin.dto;

import com.github.kegszool.user.messaging.dto.UserDto;

import java.util.List;

public record CoinExistenceResult(List<String> validCoins, List<String> invalidCoins, UserDto user) {}