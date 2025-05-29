package com.github.kegszool.notificaiton.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.kegszool.notificaiton.TriggeredNotificationBuffer;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.notificaiton.active.ActiveNotificationCacheService;

@Log4j2
@Component
public class RecurringNotificationHandler implements NotificationHandler {

    private final ActiveNotificationCacheService activeNotificationCache;
    private final TriggeredNotificationBuffer triggeredNotificationBuffer;

    @Autowired
    public RecurringNotificationHandler(
            ActiveNotificationCacheService activeNotifications,
            TriggeredNotificationBuffer triggeredNotificationBuffer
    ) {
        this.activeNotificationCache = activeNotifications;
        this.triggeredNotificationBuffer = triggeredNotificationBuffer;
    }

    @Override
    public boolean supports(NotificationDto notification) {
        return notification.isRecurring();
    }

    @Override
    public void handle(NotificationDto notification, String coinName, double currentPrice, long now) {
        triggeredNotificationBuffer.add(notification);

        synchronized (coinName.intern()) {
            activeNotificationCache.compute(coinName, existing -> {
                if (existing == null) return new CopyOnWriteArrayList<>();

                List<NotificationDto> updatedList = new ArrayList<>(existing.size());
                for (NotificationDto n : existing) {
                    if (n.isRecurring() && Objects.equals(n.getChatId(), notification.getChatId())) {
                        NotificationDto updated = new NotificationDto(
                                n.getUser(),
                                n.getMessageId(),
                                n.getChatId(),
                                n.getCoin(),
                                true,
                                false,
                                currentPrice,
                                n.getTriggeredPrice(),
                                n.getTargetPercentage(),
                                n.getDirection(),
                                now
                        );
                        updatedList.add(updated);
                    } else {
                        updatedList.add(n);
                    }
                }

                log.info("Recurring notifications updated for coin '{}' | Chat ID: {} | New initial price: {}",
                        coinName, notification.getChatId(), currentPrice);

                return new CopyOnWriteArrayList<>(updatedList);
            });
        }
    }
}