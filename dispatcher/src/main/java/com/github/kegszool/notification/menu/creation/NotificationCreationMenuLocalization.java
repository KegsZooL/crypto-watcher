package com.github.kegszool.notification.menu.creation;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class NotificationCreationMenuLocalization extends BaseMenuLocalization {

    public NotificationCreationMenuLocalization(
            @Value("${menu.notification_creation.name}") String name,

            @Value("${menu.notification_creation.title.ru}") String titleRu,
            @Value("${menu.notification_creation.title.en}") String titleEn,

            @Value("${menu.notification_creation.sections.ru}") String sectionsRu,
            @Value("${menu.notification_creation.sections.en}") String sectionsEn
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