package com.github.kegszool.notification.menu.set_notification;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class SetCoinNotificationMenuLocalization extends BaseMenuLocalization {

    public SetCoinNotificationMenuLocalization(
            @Value("${menu.set_coin_notification.name}") String name,

            @Value("${menu.set_coin_notification.title.ru}") String titleRu,
            @Value("${menu.set_coin_notification.title.en}") String titleEn,

            @Value("${menu.set_coin_notification.sections.ru}") String sectionsRu,
            @Value("${menu.set_coin_notification.sections.en}") String sectionsEn
    ) {
        super(
                name,
                Map.of(
                        "ru", titleRu,
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