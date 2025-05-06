package com.github.kegszool.notification.deletion.messaging;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.notification.messaging.dto.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationIdentifierDto {

    private Long userTelegramId;
    private Integer messageId;
    private Long chatId;
    private CoinDto coin;

    private boolean isRecurring;

    private double initialPrice;
    private BigDecimal targetPercentage;
    private Direction direction;
}