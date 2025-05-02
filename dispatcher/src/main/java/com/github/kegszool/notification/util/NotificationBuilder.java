package com.github.kegszool.notification.util;

import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.user.messaging.dto.UserDto;
import org.telegram.telegrambots.meta.api.objects.User;
import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class NotificationBuilder {

    public NotificationDto build(
            User user,
            Integer messageId,
            Long chatId,
            String coin,
            Optional<Boolean> maybeRecurring,
            BigDecimal percentage,
            Direction direction
    ) {
        return new NotificationDto(
                new UserDto(user.getId(), user.getFirstName(), user.getLastName()),
                messageId,
                chatId,
                new CoinDto(coin),
                maybeRecurring.orElse(false),
                false,
                0,
                percentage,
                direction
        );
    }
}