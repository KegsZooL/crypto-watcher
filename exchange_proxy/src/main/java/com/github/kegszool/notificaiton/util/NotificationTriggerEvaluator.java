package com.github.kegszool.notificaiton.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;

@Log4j2
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

        log.info("Notification check | Coin: {} | Chat ID: {} | Direction: {} | Initial: {} | %: {} | Target: {} | Current: {}",
                notification.getCoin().getName(), notification.getChatId(), notification.getDirection(),
                initialPrice, targetPercent, targetPrice, currentPrice);

        return switch (notification.getDirection()) {
            case Up -> currentPrice >= targetPrice;
            case Down -> currentPrice <= targetPrice;
        };
    }
}