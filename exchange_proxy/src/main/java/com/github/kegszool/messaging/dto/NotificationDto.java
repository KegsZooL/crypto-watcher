package com.github.kegszool.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private UserDto user;
    private CoinDto coin;

    private boolean isActive = true, isRecurring;
    private BigDecimal targetPercentage;

    private Direction direction;
}