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
            @Value("${menu.notification.sections.en}") String sectionsEn,

            @Value("${menu.notification.answer_messages.up.ru}") String increasedMsgRu,
            @Value("${menu.notification.answer_messages.up.en}") String increasedMsgEn,
            @Value("${menu.notification.answer_messages.up.msg_type}") String increasedMsgType,

            @Value("${menu.notification.answer_messages.down.ru}") String decreasedMsgRu,
            @Value("${menu.notification.answer_messages.down.en}") String decreasedMsgEn,
            @Value("${menu.notification.answer_messages.down.msg_type}") String decreasedMsgType
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
                Map.of(
                        increasedMsgType, Map.of(
                                "ru", increasedMsgRu,
                                "en", increasedMsgEn
                        ),
                        decreasedMsgType, Map.of(
                                "ru", decreasedMsgRu,
                                "en", decreasedMsgEn
                        )
                )
        );
    }
}