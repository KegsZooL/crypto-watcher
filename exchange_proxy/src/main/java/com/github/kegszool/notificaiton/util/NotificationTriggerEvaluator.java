package com.github.kegszool.notificaiton.util;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class NotificationTriggerEvaluator {

    public boolean isTriggered(NotificationDto notification, BigDecimal currentPrice) {

        BigDecimal initialPrice = BigDecimal.valueOf(notification.getInitialPrice());
        BigDecimal targetPercent = notification.getTargetPercentage();

        BigDecimal percentChange = initialPrice
                .multiply(targetPercent)
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);

        BigDecimal targetPrice = switch (notification.getDirection()) {
            case Up -> initialPrice.add(percentChange);
            case Down -> initialPrice.subtract(percentChange);
        };

        return switch (notification.getDirection()) {
            case Up -> currentPrice.compareTo(targetPrice) >= 0;
            case Down -> currentPrice.compareTo(targetPrice) <= 0;
        };
    }
}
