package com.github.kegszool.localization.menu.impl;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.menu.BaseMenuLocalization;

@Component
public class EditCoinSelectionMenuLocalization extends BaseMenuLocalization {

    public EditCoinSelectionMenuLocalization(
            @Value("${menu.edit_coin_sections.name}") String name,

            @Value("${menu.edit_coin_sections.title.ru}") String titleRu,
            @Value("${menu.edit_coin_sections.title.en}") String titleEn,

            @Value("${menu.edit_coin_sections.sections.ru}") String sectionsRu,
            @Value("${menu.edit_coin_sections.sections.en}") String sectionsEn
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
