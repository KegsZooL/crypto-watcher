package com.github.kegszool.notification.setting;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.BaseMenuLocalization;

@Component
public class SetCoinNotificationMenuLocalization extends BaseMenuLocalization {

    public SetCoinNotificationMenuLocalization(
            @Value("${menu.set_coin_notification.name}") String name,

            @Value("${menu.set_coin_notification.title.ru}") String titleRu,
            @Value("${menu.set_coin_notification.title.en}") String titleEn,

            @Value("${menu.set_coin_notification.sections.ru}") String sectionsRu,
            @Value("${menu.set_coin_notification.sections.en}") String sectionsEn,

            @Value("${menu.set_coin_notification.answer_messages.from_command.success.ru}")
            String fromCommandSuccessRu,

            @Value("${menu.set_coin_notification.answer_messages.from_command.success.en}")
            String fromCommandSuccessEn,

            @Value("${menu.set_coin_notification.answer_messages.from_command.success.msg_type}")
            String fromCommandSuccessMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_command.invalid_command.ru}")
            String fromCommandErrorRu,

            @Value("${menu.set_coin_notification.answer_messages.from_command.invalid_command.en}")
            String fromCommandErrorEn,

            @Value("${menu.set_coin_notification.answer_messages.from_command.invalid_command.msg_type}")
            String fromCommandErrorMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_command.not_exists.ru}")
            String fromCommandNotExistsRu,

            @Value("${menu.set_coin_notification.answer_messages.from_command.not_exists.en}")
            String fromCommandNotExistsEn,

            @Value("${menu.set_coin_notification.answer_messages.from_command.not_exists.msg_type}")
            String fromCommandNotExistsMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.created.ru}")
            String fromMenuSuccessCreatedRu,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.created.en}")
            String fromMenuSuccessCreatedEn,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.created.msg_type}")
            String fromMenuSuccessCreatedMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.invalid_percentage.ru}")
            String fromMenuInvalidPercentageRu,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.invalid_percentage.en}")
            String fromMenuInvalidPercentageEn,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.invalid_percentage.msg_type}")
            String fromMenuInvalidPercentageMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.coin_not_selected.ru}")
            String fromMenuCoinNotSelectedRu,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.coin_not_selected.en}")
            String fromMenuCoinNotSelectedEn,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.coin_not_selected.msg_type}")
            String fromMenuCoinNotSelectedMsgType
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
                        fromCommandSuccessMsgType, Map.of(
                                "ru", fromCommandSuccessRu,
                                "en", fromCommandSuccessEn
                        ),
                        fromCommandErrorMsgType, Map.of(
                                "ru", fromCommandErrorRu,
                                "en", fromCommandErrorEn
                        ),
                        fromMenuSuccessCreatedMsgType, Map.of(
                                "ru", fromMenuSuccessCreatedRu,
                                "en", fromMenuSuccessCreatedEn
                        ),
                        fromMenuInvalidPercentageMsgType, Map.of(
                                "ru", fromMenuInvalidPercentageRu,
                                "en", fromMenuInvalidPercentageEn
                        ),
                        fromMenuCoinNotSelectedMsgType, Map.of(
                                "ru", fromMenuCoinNotSelectedRu,
                                "en", fromMenuCoinNotSelectedEn
                        ),
                        fromCommandNotExistsMsgType, Map.of(
                                "ru", fromCommandNotExistsRu,
                                "en", fromCommandNotExistsEn
                        )
                )
        );
    }
}