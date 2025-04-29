package com.github.kegszool.notification.menu.deletion;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class NotificationDeletionMenuLocalization extends BaseMenuLocalization {

    public NotificationDeletionMenuLocalization(
            @Value("${menu.notification_deletion.name}") String name,

            @Value("${menu.notification_deletion.title.ru}") String titleRu,
            @Value("${menu.notification_deletion.title.en}") String titleEn,

            @Value("${menu.notification_deletion.sections.ru}") String sectionsRu,
            @Value("${menu.notification_deletion.sections.en}") String sectionsEn
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