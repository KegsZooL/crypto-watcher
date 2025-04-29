package com.github.kegszool.notification.menu.main;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class NotificationMenuLocalization extends BaseMenuLocalization {

    public NotificationMenuLocalization(
            @Value("${menu.notification.name}") String name,

            @Value("${menu.notification.title.ru}") String titleRu,
            @Value("${menu.notification.title.en}") String titleEn,

            @Value("${menu.notification.sections.ru}") String sectionsRu,
            @Value("${menu.notification.sections.en}") String sectionsEn
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