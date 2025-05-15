package com.github.kegszool.language.menu;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

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