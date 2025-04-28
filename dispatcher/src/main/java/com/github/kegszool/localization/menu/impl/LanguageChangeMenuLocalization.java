package com.github.kegszool.localization.menu.impl;

import com.github.kegszool.localization.menu.BaseMenuLocalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LanguageChangeMenuLocalization extends BaseMenuLocalization {

    public LanguageChangeMenuLocalization(
            @Value("${menu.language_change.name}") String name,
            @Value("${menu.language_change.title.ru}") String titleRu,
            @Value("${menu.language_change.title.en}") String titleEn,
            @Value("${menu.language_change.sections.ru}") String sectionsRu,
            @Value("${menu.language_change.sections.en}") String sectionsEn
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