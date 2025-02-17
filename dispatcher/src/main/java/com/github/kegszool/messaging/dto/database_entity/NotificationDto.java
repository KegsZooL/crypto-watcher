package com.github.kegszool.messaging.dto.database_entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private UserDto user;
    private CoinDto coin;

    private boolean isActive = true, isRecurring;
    private BigDecimal targetPrice, targetPercentage;

    private Direction direction;
}