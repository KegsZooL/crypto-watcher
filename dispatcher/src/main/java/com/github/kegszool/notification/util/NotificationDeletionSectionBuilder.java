package com.github.kegszool.notification.util;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class NotificationDeletionSectionBuilder implements SectionBuilder {

    private final String callbackDataPrefix;
    private final String entryFormatRu;
    private final String entryFormatEn;

    public NotificationDeletionSectionBuilder(
            @Value("${menu.action.noop_prefix}") String callbackDataPrefix,
            @Value("${menu.notification_deletion.title.entryFormat.ru}") String entryFormatRu,
            @Value("${menu.notification_deletion.title.entryFormat.en}") String entryFormatEn
    ) {
        this.callbackDataPrefix = callbackDataPrefix;
        this.entryFormatRu = entryFormatRu;
        this.entryFormatEn = entryFormatEn;
    }

    @Override
    public String buildSectionsConfig(UserData userData, String locale) {

        List<NotificationDto> notifications = userData.getNotifications();

        if (notifications == null || notifications.isEmpty()) {
            return "";
        }

        List<String> sections = new ArrayList<>();
        for (NotificationDto notification: notifications) {
            String sectionEntry = buildEntry(notification, locale);
            sections.add(sectionEntry);
        }
        return String.join(",", sections);
    }

    private String buildEntry(NotificationDto notification, String locale) {
        CoinDto coin = notification.getCoin();
        String coinName = coin.getName();

        String formattedPercentage = NotificationValueFormatter.formatPercentage(notification.getTargetPercentage().doubleValue());
        String formattedPrice = NotificationValueFormatter.formatPrice(notification.getInitialPrice());

        String entryFormat = "ru".equals(locale) ? entryFormatRu : entryFormatEn;

        String directionArrow = notification.getDirection() == Direction.Up ? "\uD83D\uDCB9" : "\uD83D\uDD3B";
        String title = String.format(
                entryFormat,
                coinName,
                directionArrow,
                formattedPercentage,
                formattedPrice,
                notification.isRecurring() ? "🔁" : "🔂"
        );
        String callbackData = callbackDataPrefix + coinName;
        return String.format("%s:%s", callbackData, title);
    }
}