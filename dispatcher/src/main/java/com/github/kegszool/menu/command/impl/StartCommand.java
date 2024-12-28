package com.github.kegszool.menu.command.impl;

import com.github.kegszool.menu.Menu;
import com.github.kegszool.menu.MenuNavigationService;
import com.github.kegszool.menu.MenuRegistry;
import com.github.kegszool.menu.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {

    private static final String START_COMMAND = "/start";

    private final MenuRegistry menuRegistry;
    private final MenuNavigationService navigationService;

    @Value("${menu.pages[4].main}")
    private String MAIN_MENU_NAME;

    @Autowired
    public StartCommand(
            MenuRegistry menuRegistry,
            MenuNavigationService navigationService
    ) {
        this.menuRegistry = menuRegistry;
        this.navigationService = navigationService;
    }

    @Override
    public boolean canHandle(String command) {
        return START_COMMAND.equals(command);
    }

    @Override
    public PartialBotApiMethod<?> execute(Update update) {
        long chatId = update.getMessage().getChatId();
        navigationService.pushMenu(chatId, MAIN_MENU_NAME);
        Menu mainMenu = menuRegistry.getMenu(MAIN_MENU_NAME);
        var answerMessage = new SendMessage(String.valueOf(chatId), mainMenu.getTitle());
        answerMessage.setReplyMarkup(mainMenu.get());
        return answerMessage;
    }
}
