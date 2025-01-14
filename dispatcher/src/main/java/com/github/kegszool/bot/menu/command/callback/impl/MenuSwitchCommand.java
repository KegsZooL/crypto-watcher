package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.MenuNavigationService;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;


//TODO: добавить логирование

@Component
@Log4j2
public class MenuSwitchCommand extends CallbackCommand {

    private final MenuRegistry menuRegistry;
    private final MenuNavigationService menuNavigationService;
    private final MessageUtils messageUtils;

    public MenuSwitchCommand(
            MenuRegistry menuRegistry,
            MenuNavigationService menuNavigationService,
            MessageUtils messageUtils
    ) {
        this.menuRegistry = menuRegistry;
        this.menuNavigationService = menuNavigationService;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return menuRegistry.isContained(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        String currentMenuName = query.getData();
        menuNavigationService.pushMenu(chatId, currentMenuName);
        return messageUtils.createEditMessageTextByMenuName(query, currentMenuName);
    }
}
