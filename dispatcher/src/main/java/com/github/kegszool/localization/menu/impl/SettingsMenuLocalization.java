package com.github.kegszool.localization.menu.impl;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.menu.BaseMenuLocalization;

@Component
public class SettingsMenuLocalization extends BaseMenuLocalization {

    public SettingsMenuLocalization(
            @Value("${menu.settings.name}") String name,
            @Value("${menu.settings.title.ru}") String titleRu,
            @Value("${menu.settings.title.en}") String titleEn,
            @Value("${menu.settings.sections.ru}") String sectionsRu,
            @Value("${menu.settings.sections.en}") String sectionsEn
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