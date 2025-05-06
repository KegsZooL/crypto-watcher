package com.github.kegszool.notification;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.Direction;

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