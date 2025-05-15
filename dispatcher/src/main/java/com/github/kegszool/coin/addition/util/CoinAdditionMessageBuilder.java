package com.github.kegszool.coin.addition.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class CoinAdditionMessageBuilder {

    private final String menuName;
    private final String successMsgType, errorMsgType;
   	private final LocalizationService localizationService;

    @Autowired
    public CoinAdditionMessageBuilder(
            @Value("${menu.coin_addition.name}") String menuName,
            @Value("${menu.coin_addition.answer_messages.success.msg_type}") String successMsgType,
            @Value("${menu.coin_addition.answer_messages.error.msg_type}") String errorMsgType,
            LocalizationService localizationService
    ) {
        this.menuName = menuName;
        this.successMsgType = successMsgType;
        this.errorMsgType = errorMsgType;
        this.localizationService = localizationService;
    }

    public SendMessage buildSuccessMessage(Message msg, List<String> coins) {
        Long chatId = msg.getChatId();
        String successMsg = localizationService.getAnswerMessage(menuName, successMsgType, chatId.toString());
        return SendMessage.builder()
                .chatId(chatId)
                .text(successMsg + String.join(", ", coins))
                .build();
    }

    public SendMessage buildErrorMessage(Message msg) {
        Long chatId = msg.getChatId();
        String errorMsg = localizationService.getAnswerMessage(menuName, errorMsgType, chatId.toString());
        return SendMessage.builder()
                .chatId(chatId)
                .text(errorMsg)
                .build();
    }
}