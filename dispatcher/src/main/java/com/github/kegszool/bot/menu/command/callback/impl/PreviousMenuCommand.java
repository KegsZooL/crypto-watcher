package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.service.MenuNavigationService;
import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class PreviousMenuCommand extends CallbackCommand {

    @Value("${menu.actions.back}")
    private String BACK_COMMAND;

    private final MessageUtils messageUtils;
    private final MenuNavigationService menuNavigationService;

    @Autowired
    public PreviousMenuCommand(
            MessageUtils messageUtils,
            MenuNavigationService menuNavigationService
    ) {
        this.messageUtils = messageUtils;
        this.menuNavigationService = menuNavigationService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return BACK_COMMAND.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        var chatId = messageUtils.extractChatId(query);
        var previousMenuName = menuNavigationService.popMenu(chatId);
        logCommand(chatId, previousMenuName);
        return messageUtils.createEditMessageByMenuName(query, previousMenuName);
    }

    private void logCommand(String chatId, String previousMenuName) {
        var msg = String.format("The command to switch to the previous menu " +
                  "\"%s\" of the chat \"%s\" has been worked out.", previousMenuName, chatId);
        log.info(msg);
    }
}