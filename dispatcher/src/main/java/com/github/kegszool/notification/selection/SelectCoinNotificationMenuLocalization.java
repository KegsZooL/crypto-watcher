package com.github.kegszool.notification.selection;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class SelectCoinNotificationMenuLocalization extends BaseMenuLocalization {

    public SelectCoinNotificationMenuLocalization(
            @Value("${menu.select_coin_notification.name}") String name,

            @Value("${menu.select_coin_notification.title.ru}") String titleRu,
            @Value("${menu.select_coin_notification.title.en}") String titleEn,

            @Value("${menu.select_coin_notification.sections.ru}") String sectionsRu,
            @Value("${menu.select_coin_notification.sections.en}") String sectionsEn
    ) {
        super(
                name,
                Map.of(
                        "ru",titleRu,
                        "en", titleEn
                ),
                Map.of(
                        "ru", sectionsRu,
                        "en", sectionsEn
                ),
                null
        );
    }

}