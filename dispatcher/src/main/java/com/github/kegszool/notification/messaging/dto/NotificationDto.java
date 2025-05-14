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
    private Integer messageId;
    private Long chatId;
    private CoinDto coin;

    private boolean isRecurring;
    private boolean isTriggered;

    private double initialPrice;
    private double triggeredPrice;
    private BigDecimal targetPercentage;
    private Direction direction;

    private long lastTriggeredTime;
}