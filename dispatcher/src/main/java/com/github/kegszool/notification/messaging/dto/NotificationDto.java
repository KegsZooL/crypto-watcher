package com.github.kegszool.notification.messaging.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.user.messaging.dto.UserDto;

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