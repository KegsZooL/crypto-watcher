package com.github.kegszool.command.text;

import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class StartCommand extends TextCommand {

    @Value("${menu.main.name}")
    private String MAIN_MENU_NAME;

    private static final String START_COMMAND = "/start";

    private final MessageUtils messageUtils;

    @Autowired
    public StartCommand(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return START_COMMAND.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        String chatId = messageUtils.extractChatId(update);
        var answerMessage = messageUtils.recordAndCreateMessageByMenuName(chatId, MAIN_MENU_NAME);
        log.info("The start command has been worked out for the current chat: {}", chatId);
        return answerMessage;
    }
}