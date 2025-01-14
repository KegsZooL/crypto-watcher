package com.github.kegszool.bot.menu.command.text.impl;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.MenuNavigationService;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import com.github.kegszool.bot.menu.command.text.TextCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j2
public class StartCommand extends TextCommand {

    private static final String START_COMMAND = "/start";

    private final MenuRegistry menuRegistry;
    private final MenuNavigationService navigationService;

    @Value("${menu.pages[4].main}")
    private String MAIN_MENU_NAME;

    public StartCommand(MenuRegistry menuRegistry, MenuNavigationService navigationService) {
        this.menuRegistry = menuRegistry;
        this.navigationService = navigationService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return START_COMMAND.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        navigationService.pushMenu(chatId, MAIN_MENU_NAME);
        Menu mainMenu = menuRegistry.getMenu(MAIN_MENU_NAME);
        var answerMessage = new SendMessage(String.valueOf(chatId), mainMenu.getTitle());
        answerMessage.setReplyMarkup(mainMenu.get());
        return answerMessage;
    }
}
