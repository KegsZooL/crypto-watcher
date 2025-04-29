package com.github.kegszool.coin.addition.menu;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class CoinAdditionMenuLocalization extends BaseMenuLocalization {

    public CoinAdditionMenuLocalization(
            @Value("${menu.coin_addition.name}") String name,

            @Value("${menu.coin_addition.title.ru}") String titleRu,
            @Value("${menu.coin_addition.title.en}") String titleEn,

            @Value("${menu.coin_addition.sections.ru}") String sectionsRu,
            @Value("${menu.coin_addition.sections.en}") String sectionsEn,

            @Value("${menu.coin_addition.answer_messages.success.ru}") String successMsgRu,
            @Value("${menu.coin_addition.answer_messages.success.en}") String successMsgEn,

            @Value("${menu.coin_addition.answer_messages.error.ru}") String errorMsgRu,
            @Value("${menu.coin_addition.answer_messages.error.en}") String errorMsgEn,

            @Value("${menu.coin_addition.answer_messages.all_coins_added.ru}") String allCoinsAddedRu,
            @Value("${menu.coin_addition.answer_messages.all_coins_added.en}") String allCoinsAddedEn,

            @Value("${menu.coin_addition.answer_messages.some_coins_added.ru}") String someCoinsAddedRu,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.en}") String someCoinsAddedEn,

            @Value("${menu.coin_addition.answer_messages.no_coins_added.ru}") String noCoinsAddedRu,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.en}") String noCoinsAddedEn,

            @Value("${menu.coin_addition.answer_messages.success.msg_type}") String successMsgType,
            @Value("${menu.coin_addition.answer_messages.error.msg_type}") String errorMsgType,

            @Value("${menu.coin_addition.answer_messages.all_coins_added.msg_type}") String allCoinsAddedMsgType,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.msg_type}") String someCoinsAddedMsgType,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.msg_type}") String noCoinsAddedMsgType
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
                        successMsgType, Map.of(
                                "ru", successMsgRu,
                                "en", successMsgEn
                        ),
                        errorMsgType, Map.of(
                                "ru", errorMsgRu,
                                "en", errorMsgEn
                        ),
                        allCoinsAddedMsgType, Map.of(
                                "ru", allCoinsAddedRu,
                                "en", allCoinsAddedEn
                        ),
                        someCoinsAddedMsgType, Map.of(
                                "ru", someCoinsAddedRu,
                                "en", someCoinsAddedEn
                        ),
                        noCoinsAddedMsgType, Map.of(
                                "ru", noCoinsAddedRu,
                                "en", noCoinsAddedEn
                        )
                )
        );
    }
}