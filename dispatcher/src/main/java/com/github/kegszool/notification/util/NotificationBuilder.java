package com.github.kegszool.notification.util;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

import java.math.BigDecimal;
import org.telegram.telegrambots.meta.api.objects.User;

public class NotificationBuilder {

    public static NotificationDto build(
            User user,
            Integer messageId,
            Long chatId,
            String coin,
            boolean isRecurring,
            BigDecimal percentage,
            Direction direction
    ) {
        return new NotificationDto(
                new UserDto(user.getId(), user.getFirstName(), user.getLastName()),
                messageId,
                chatId,
                new CoinDto(coin),
                isRecurring,
                false,
                0,
                percentage,
                direction
        );
    }
}
