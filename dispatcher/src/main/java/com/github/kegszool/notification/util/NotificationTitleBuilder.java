package com.github.kegszool.notification.util;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.util.TitleBuilder;
import com.github.kegszool.user.messaging.dto.UserData;

import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class NotificationTitleBuilder implements TitleBuilder {

    private final String recurringTitleRu;
    private final String recurringTitleEn;

    private final String oneTimeTitleRu;
    private final String oneTimeTitleEn;

    private final String emptyTitleRu;
    private final String emptyTitleEn;

    private final String entryFormatRu;
    private final String entryFormatEn;

    @Autowired
    public NotificationTitleBuilder(
            @Value("${menu.notification.title.recurringTitle.ru}") String recurringTitleRu,
            @Value("${menu.notification.title.recurringTitle.en}") String recurringTitleEn,
            @Value("${menu.notification.title.oneTimeTitle.ru}") String oneTimeTitleRu,
            @Value("${menu.notification.title.oneTimeTitle.en}") String oneTimeTitleEn,
            @Value("${menu.notification.title.emptyTitle.ru}") String emptyTitleRu,
            @Value("${menu.notification.title.emptyTitle.en}") String emptyTitleEn,
            @Value("${menu.notification.title.entryFormat.ru}") String entryFormatRu,
            @Value("${menu.notification.title.entryFormat.en}") String entryFormatEn
    ) {
        this.recurringTitleRu = recurringTitleRu;
        this.recurringTitleEn = recurringTitleEn;
        this.oneTimeTitleRu = oneTimeTitleRu;
        this.oneTimeTitleEn = oneTimeTitleEn;
        this.emptyTitleRu = emptyTitleRu;
        this.emptyTitleEn = emptyTitleEn;
        this.entryFormatRu = entryFormatRu;
        this.entryFormatEn = entryFormatEn;
    }

    @Override
    public String buildTitle(UserData userData, String language) {

        List<NotificationDto> notifications = userData.getNotifications();

        List<String> recurringTitles = new ArrayList<>();
        List<String> oneTimeTitles = new ArrayList<>();

        for (NotificationDto notification : notifications) {
            String entry = buildEntry(language, notification);
            if (notification.isRecurring()) {
                recurringTitles.add(entry);
            } else {
                oneTimeTitles.add(entry);
            }
        }

        StringBuilder sections = new StringBuilder();

        if (!recurringTitles.isEmpty()) {
            String recurringTitle = "ru".equals(language) ? recurringTitleRu : recurringTitleEn;
            sections.append(recurringTitle).append("\n\n");
            sections.append(String.join("\n", recurringTitles)).append("\n\n");
        }

        if (!oneTimeTitles.isEmpty()) {
            String oneTimeTitle = "ru".equals(language) ? oneTimeTitleRu : oneTimeTitleEn;
            sections.append(oneTimeTitle).append("\n\n");
            sections.append(String.join("\n", oneTimeTitles)).append("\n");
        }

        if (sections.length() == 0) {
            return "ru".equals(language) ? emptyTitleRu : emptyTitleEn;
        }
        return sections.toString().trim();
    }

    private String buildEntry(String locale, NotificationDto notification) {

        String coinName = notification.getCoin().getName();
        String directionArrow = notification.getDirection() == Direction.Up ? "\uD83D\uDCB9" : "\uD83D\uDD3B";

        double percentage = notification.getTargetPercentage().doubleValue();
        double initialPrice = notification.getInitialPrice();

        String formattedPercentage = NotificationValueFormatter.formatPercentage(percentage);

        String formattedPrice;

        if(initialPrice >= 100) {
            formattedPrice = String.format("%.2f", initialPrice);
        } else if (initialPrice >= 1) {
            formattedPrice = String.format("%.3f", initialPrice);
        } else if (initialPrice >= 0.001) {
            formattedPrice = String.format("%.4f", initialPrice);
        } else if (initialPrice >= 0.00001) {
            formattedPrice = String.format("%.6f", initialPrice);
        } else {
            formattedPrice = NotificationValueFormatter.formatPrice(initialPrice);
        }
        String entryFormat = "ru".equals(locale) ? entryFormatRu : entryFormatEn;
        return String.format(entryFormat, coinName, directionArrow, formattedPercentage, formattedPrice);
    }

    @Override
    public String getDefaultTitle() {
        return emptyTitleRu;
    }
}