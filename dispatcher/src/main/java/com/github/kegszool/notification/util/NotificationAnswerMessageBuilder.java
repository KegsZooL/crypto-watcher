package com.github.kegszool.notification.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class NotificationAnswerMessageBuilder {

    private final String fromCommandSuccessMsgType;
    private final String fromCommandErrorMsgType;
    private final String fromMenuSuccessMsgType;
    private final String fromMenuInvalidPercentageMsgType;
    private final String fromMenuCoinNotSelectedMsgType;

    private final String menuName;
    private final LocalizationService localizationService;

    @Autowired
    public NotificationAnswerMessageBuilder(
            @Value("${menu.set_coin_notification.answer_messages.from_command.success.msg_type}")
            String fromCommandSuccessMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_command.error.msg_type}")
            String fromCommandErrorMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.created.msg_type}")
            String fromMenuSuccessMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.invalid_percentage.msg_type}")
            String fromMenuInvalidPercentageMsgType,

            @Value("${menu.set_coin_notification.answer_messages.from_menu.coin_not_selected.msg_type}")
            String fromMenuCoinNotSelectedMsgType,

            @Value("${menu.set_coin_notification.name}") String menuName,
            LocalizationService localizationService
    ) {
        this.fromCommandSuccessMsgType = fromCommandSuccessMsgType;
        this.fromCommandErrorMsgType = fromCommandErrorMsgType;
        this.fromMenuSuccessMsgType = fromMenuSuccessMsgType;
        this.fromMenuInvalidPercentageMsgType = fromMenuInvalidPercentageMsgType;
        this.fromMenuCoinNotSelectedMsgType = fromMenuCoinNotSelectedMsgType;
        this.menuName = menuName;
        this.localizationService = localizationService;
    }

    public SendMessage createSuccessMsgFromCommand(Long chatId, String coin) {
        String localizedText = localizationService
                .getAnswerMessage(menuName, fromCommandSuccessMsgType, chatId.toString())
                .replace("{coin}", coin);
        return buildMessage(chatId, localizedText);
    }

    public SendMessage createErrorMsgFromCommand(Long chatId) {
        String localizedText = localizationService.getAnswerMessage(menuName, fromCommandErrorMsgType, chatId.toString());
        return buildMessage(chatId, localizedText);
    }

    public SendMessage createSuccessMsgFromMenu(Long chatId, String coin) {
        String localizedText = localizationService
                .getAnswerMessage(menuName, fromMenuSuccessMsgType, chatId.toString())
                .replace("{coin}", coin);
        return buildMessage(chatId, localizedText);
    }

    public SendMessage createInvalidPercentageMsgFromMenu(Long chatId) {
        String localizedText = localizationService.getAnswerMessage(menuName, fromMenuInvalidPercentageMsgType, chatId.toString());
        return buildMessage(chatId, localizedText);
    }

    public SendMessage createCoinNotSelectedMsgFromMenu(Long chatId) {
        String localizedText = localizationService.getAnswerMessage(menuName, fromMenuCoinNotSelectedMsgType, chatId.toString());
        return buildMessage(chatId, localizedText);
    }

    private SendMessage buildMessage(Long chatId, String text) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}