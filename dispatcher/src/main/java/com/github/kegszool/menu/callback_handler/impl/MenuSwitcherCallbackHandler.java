package com.github.kegszool.menu.callback_handler.impl;

import com.github.kegszool.menu.callback_handler.CallbackHandler;
import com.github.kegszool.menu.MenuNavigationService;
import com.github.kegszool.menu.MenuRegistry;
import com.github.kegszool.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MenuSwitcherCallbackHandler implements CallbackHandler {

    private final MenuRegistry menuRegistry;
    private final MenuNavigationService menuNavigationService;
    private final MessageUtils messageUtils;

    @Autowired
    public MenuSwitcherCallbackHandler(
            MenuRegistry menuRegistry,
            MenuNavigationService menuNavigationService,
            MessageUtils messageUtils
    ) {
        this.menuRegistry = menuRegistry;
        this.menuNavigationService = menuNavigationService;
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String menuName) {
        return menuRegistry.isContained(menuName);
    }

    @Override
    public EditMessageText handle(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        String currentMenuName = query.getData();
        menuNavigationService.pushMenu(chatId, currentMenuName);
        return messageUtils.createEditMessageTextByMenuName(query, currentMenuName);
    }
}
