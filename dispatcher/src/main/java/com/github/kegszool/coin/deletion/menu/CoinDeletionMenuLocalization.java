package com.github.kegszool.coin.deletion.menu;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class CoinDeletionMenuLocalization extends BaseMenuLocalization {

    public CoinDeletionMenuLocalization(
            @Value("${menu.coin_deletion_menu.name}") String name,

            @Value("${menu.coin_deletion_menu.title.ru}") String titleRu,
            @Value("${menu.coin_deletion_menu.title.en}") String titleEn,

            @Value("${menu.coin_deletion_menu.sections.ru}") String sectionsRu,
            @Value("${menu.coin_deletion_menu.sections.en}") String sectionsEn
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