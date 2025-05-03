package com.github.kegszool.menu.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class MenuSwitcher extends CallbackCommand {

    private final MenuRegistry menuRegistry;
    private final MenuHistoryManager menuHistoryManager;

    @Autowired
    public MenuSwitcher(MenuRegistry menuRegistry, MenuHistoryManager menuHistoryManager) {
        this.menuRegistry = menuRegistry;
        this.menuHistoryManager = menuHistoryManager;
    }

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }

        String chatId = messageUtils.extractChatId(update);
        String command = update.getCallbackQuery().getData();
        return menuRegistry.isContained(command, chatId);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return true;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String menuName = switchAndGetMenu(query);
        var message = messageUtils.createEditMessageByMenuName(query, menuName);
        log.info("Switched menu successfully. Menu name: {}", menuName);
        return message;
    }

    private String switchAndGetMenu(CallbackQuery query) {
        String chatId = messageUtils.extractChatId(query);
        String currentMenuName = query.getData();
        menuHistoryManager.recordMenu(chatId, currentMenuName);
        return currentMenuName;
    }
}