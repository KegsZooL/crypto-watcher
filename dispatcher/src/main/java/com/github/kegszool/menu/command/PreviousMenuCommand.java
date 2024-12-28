package com.github.kegszool.menu.command;

import com.github.kegszool.menu.MenuNavigationService;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class PreviousMenuCommand extends CallbackCommand {

    private final MenuNavigationService menuNavigationService;
    private final MessageUtils messageUtils;

    @Value("${menu.actions.back}")
    private String BACK_COMMAND;

    public PreviousMenuCommand(MenuNavigationService menuNavigationService, MessageUtils messageUtils) {
        this.menuNavigationService = menuNavigationService;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return BACK_COMMAND.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        Long chatId = query.getMessage().getChatId();
        String previousMenuName = menuNavigationService.popMenu(chatId);
        return messageUtils.createEditMessageTextByMenuName(query, previousMenuName);
    }
}
