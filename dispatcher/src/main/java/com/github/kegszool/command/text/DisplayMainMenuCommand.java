package com.github.kegszool.command.text;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Log4j2
@Component
public class DisplayMainMenuCommand extends TextCommand {

    private final String menuName;
    private final String command;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayMainMenuCommand(
            @Value("${menu.main.name}") String menuName,
            @Value("${bot.command.display_main_menu}") String command,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.command = command;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        String chatId = messageUtils.extractChatId(update);
        var answerMessage = messageUtils.recordAndCreateMessageByMenuName(chatId, menuName);
        log.info("The  command has been worked out for the current chat: {}", chatId);
        return answerMessage;
    }
}