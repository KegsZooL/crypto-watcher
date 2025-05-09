package com.github.kegszool.menu.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.bot.TelegramBot;
import com.github.kegszool.menu.util.MenuCommandBuilder;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Service
public class MenuCommandConfigurationService {

    private final MenuCommandBuilder menuCommandBuilder;
    private TelegramBot bot;

    @Autowired
    public MenuCommandConfigurationService(
            MenuCommandBuilder menuCommandBuilder
    ) {
        this.menuCommandBuilder = menuCommandBuilder;
    }

    public void init(TelegramBot bot) {
        if (this.bot == null) {
            this.bot = bot;
        }
        List<String> supportedLocales = List.of("ru", "en");
        supportedLocales.forEach(this::executeCommand);
    }

    public void executeCommand(String locale) {
        List<BotCommand> localizedCommands = menuCommandBuilder.build(locale);

        SetMyCommands setMyCommands = SetMyCommands.builder()
                .languageCode(locale)
                .commands(localizedCommands)
                .build();

        bot.executeMsg(setMyCommands);
    }
}