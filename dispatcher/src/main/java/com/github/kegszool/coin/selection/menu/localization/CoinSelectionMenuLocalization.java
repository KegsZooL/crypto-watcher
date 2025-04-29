package com.github.kegszool.coin.selection.menu.localization;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class CoinSelectionMenuLocalization extends BaseMenuLocalization {

    public CoinSelectionMenuLocalization(
            @Value("${menu.coin_selection.name}") String name,

            @Value("${menu.coin_selection.title.ru}") String titleRu,
            @Value("${menu.coin_selection.title.en}") String titleEn,

            @Value("${menu.coin_selection.sections.ru}") String sectionsRu,
            @Value("${menu.coin_selection.sections.en}") String sectionsEn,

            @Value("${menu.coin_selection.answer_message.ru}") String answerMessageRu,
            @Value("${menu.coin_selection.answer_message.en}") String answerMessageEn
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
                        "default", Map.of(
                                "ru", answerMessageRu,
                                "en", answerMessageEn
                        )
                )
        );
    }
}