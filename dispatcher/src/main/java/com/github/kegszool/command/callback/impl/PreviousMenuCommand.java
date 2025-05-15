package com.github.kegszool.command.callback.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.service.MenuHistoryManager;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Log4j2
@Component
public class PreviousMenuCommand extends CallbackCommand {

    @Value("${menu.action.back}")
    private String BACK_COMMAND;

    private final MenuHistoryManager menuHistoryManager;

    @Autowired
    public PreviousMenuCommand(MenuHistoryManager menuHistoryManager) {
        this.menuHistoryManager = menuHistoryManager;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return BACK_COMMAND.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        var chatId = messageUtils.extractChatId(query);
        var previousMenuName = menuHistoryManager.removeMenu(chatId);
        logCommand(chatId, previousMenuName);
        return messageUtils.createEditMessageByMenuName(query, previousMenuName);
    }

    private void logCommand(String chatId, String previousMenuName) {
        var messagePattern = "The command to switch to the previous menu \"%s\" for chat \"%s\"" +
                            " has been successfully executed.";
        var msg = String.format(messagePattern, previousMenuName, chatId);
        log.info(msg);
    }
}