package com.github.kegszool.menu.callback_handler.impl;

import com.github.kegszool.menu.callback_handler.CallbackHandler;
import com.github.kegszool.menu.MenuNavigationService;
import com.github.kegszool.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class BackCommandCallbackHandler implements CallbackHandler {

    private final MenuNavigationService menuNavigationService;
    private final MessageUtils messageUtils;

    @Autowired
    public BackCommandCallbackHandler(
        MenuNavigationService menuNavigationService,
        MessageUtils messageUtils
    ) {
        this.menuNavigationService = menuNavigationService;
        this.messageUtils = messageUtils;
    }

    @Value("${menu.actions.back}")
    private String BACK_COMMAND;

    @Override
    public boolean canHandle(String command) {
        return BACK_COMMAND.equals(command);
    }

    @Override
    public EditMessageText handle(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        String previousMenuName = menuNavigationService.popMenu(chatId);
        return messageUtils.createEditMessageTextByMenuName(query, previousMenuName);
    }
}