package com.github.kegszool.messaging.dto.command_entity;

import com.github.kegszool.messaging.dto.UserDto;

import java.util.List;

public record CoinExistenceResult(List<String> validCoins, List<String> invalidCoins, UserDto user) {}