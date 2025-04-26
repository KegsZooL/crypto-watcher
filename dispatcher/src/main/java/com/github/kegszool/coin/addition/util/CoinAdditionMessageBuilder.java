package com.github.kegszool.coin.addition.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import com.github.kegszool.LocalizationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

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
        String successMsg = localizationService.getAnswerMessage(menuName, successMsgType);
        return SendMessage.builder()
                .chatId(msg.getChatId())
                .text(successMsg + String.join(", ", coins))
                .build();
    }

    public SendMessage buildErrorMessage(Message msg) {
        String errorMsg = localizationService.getAnswerMessage(menuName, errorMsgType);
        return SendMessage.builder()
                .chatId(msg.getChatId())
                .text(errorMsg)
                .build();
    }
}