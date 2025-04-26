package com.github.kegszool.coin.dto;

import com.github.kegszool.user.dto.UserDto;

import java.util.List;

public record CoinExistenceResult(List<String> validCoins, List<String> invalidCoins, UserDto user) {}