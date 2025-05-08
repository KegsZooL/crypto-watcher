package com.github.kegszool.notificaiton.util;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;

@Component
public class NotificationTriggerEvaluator {

    public boolean isTriggered(NotificationDto notification, double currentPrice) {

        double initialPrice = notification.getInitialPrice();
        double targetPercent = notification.getTargetPercentage().doubleValue();

        double percentChange = initialPrice * targetPercent / 100.0;
        double targetPrice = switch (notification.getDirection()) {
            case Up -> initialPrice + percentChange;
            case Down -> initialPrice - percentChange;
        };

        return switch (notification.getDirection()) {
            case Up -> currentPrice >= targetPrice;
            case Down -> currentPrice <= targetPrice;
        };
    }
}