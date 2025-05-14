package com.github.kegszool.menu.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class UnknownMessageBuilder {

    private final String mainMenuName;
    private final String msgType;
    private final ReplyKeyboardService keyboardService;
    private final MessageUtils messageUtils;

    @Autowired
    public UnknownMessageBuilder(
            @Value("${menu.main.name}") String mainMenuName,
            @Value("${menu.main.answer_messages.not_supported_command.msg_type}") String msgType,
            ReplyKeyboardService keyboardService,
            MessageUtils messageUtils
    ) {
        this.mainMenuName = mainMenuName;
        this.msgType = msgType;
        this.keyboardService = keyboardService;
        this.messageUtils = messageUtils;
    }

    public HandlerResult.Success build(Update update) {
        String chatId = messageUtils.extractChatId(update);
        SendMessage fallbackMessage = messageUtils.createSendMessage(mainMenuName, msgType, chatId);
        keyboardService.attachKeyboard(fallbackMessage, chatId);
        return new HandlerResult.Success(fallbackMessage);
    }
}