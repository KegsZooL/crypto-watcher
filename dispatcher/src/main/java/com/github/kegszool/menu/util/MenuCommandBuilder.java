package com.github.kegszool.menu.util;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class MenuCommandBuilder {

    private final String menuCommand;
    private final LocalizationService localizationService;

    @Autowired
    public MenuCommandBuilder(
            @Value("${menu.main.command}") String menuCommand,
            LocalizationService localizationService
    ) {
        this.menuCommand = menuCommand;
        this.localizationService = localizationService;
    }

    public List<BotCommand> build(String locale) {
        return List.of(
                new BotCommand(menuCommand, localizationService.getCommandDescription(menuCommand, locale))
        );
    }
}