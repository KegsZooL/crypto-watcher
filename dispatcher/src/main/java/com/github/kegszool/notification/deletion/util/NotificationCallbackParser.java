package com.github.kegszool.notification.deletion.util;

import java.math.BigDecimal;
import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.deletion.messaging.NotificationIdentifierDto;

public class NotificationCallbackParser {

    public static NotificationIdentifierDto parse(String callbackData, String callbackPrefix, Long chatId) {
        String payload = callbackData.substring(callbackPrefix.length());
        String[] parts = payload.split("_");

        BigDecimal targetPercentage = new BigDecimal(parts[0]);
        Direction direction = Direction.valueOf(parts[1]);
        boolean isRecurring = Boolean.parseBoolean(parts[2]);
        double initialPrice = Double.parseDouble(parts[3]);

        Integer notificationMsgId = Integer.parseInt(parts[4]);
        Long userTelegramId = Long.parseLong(parts[5]);
        String coinName = parts[6];

        CoinDto coinDto = new CoinDto();
        coinDto.setName(coinName);

        return new NotificationIdentifierDto(
                userTelegramId,
                notificationMsgId,
                chatId,
                coinDto,
                isRecurring,
                initialPrice,
                targetPercentage,
                direction
        );
    }
}