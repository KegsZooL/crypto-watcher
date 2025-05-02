package com.github.kegszool.messaging.dto.database_entity;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

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
    private BigDecimal targetPercentage;
    private Direction direction;
}