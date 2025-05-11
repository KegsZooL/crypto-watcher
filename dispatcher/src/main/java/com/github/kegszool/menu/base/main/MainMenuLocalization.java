package com.github.kegszool.menu.base.main;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class MainMenuLocalization extends BaseMenuLocalization {

    public MainMenuLocalization(
            @Value("${menu.main.name}") String name,

            @Value("${menu.main.title.ru}") String titleRu,
            @Value("${menu.main.title.en}") String titleEn,

            @Value("${menu.main.sections.ru}") String sectionsRu,
            @Value("${menu.main.sections.en}") String sectionsEn,

            @Value("${menu.main.answer_messages.not_supported_command.ru}") String notSupportedRu,
            @Value("${menu.main.answer_messages.not_supported_command.en}") String notSupportedEn,
            @Value("${menu.main.answer_messages.not_supported_command.msg_type}") String notSupportedMsgType
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
                Map.of(
                        notSupportedMsgType, Map.of(
                                "ru", notSupportedRu,
                                "en", notSupportedEn
                        )
                )
        );
    }
}