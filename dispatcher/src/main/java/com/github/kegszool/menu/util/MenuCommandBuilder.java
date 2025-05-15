package com.github.kegszool.menu.util;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class MenuCommandBuilder {

    private final String mainMenu;
    private final String editCoinSections;
    private final String notification;
    private final String creationNotification;
    private final String deletionNotification;
    private final String settings;
    private final String favoriteCoins;
    private final LocalizationService localizationService;

    @Autowired
    public MenuCommandBuilder(
            @Value("${menu.main.command}") String mainMenu,
            @Value("${menu.edit_coin_sections.command}") String editCoinSections,
            @Value("${menu.notification.command}") String notification,
            @Value("${menu.notification_creation.display_menu_command}") String creationNotification,
            @Value("${menu.notification_deletion.command}") String deletionNotification,
            @Value("${menu.settings.command}") String settings,
            @Value("${menu.coin_selection.command}") String favoriteCoins,
            LocalizationService localizationService
    ) {
        this.mainMenu = mainMenu;
        this.editCoinSections = editCoinSections;
        this.notification = notification;
        this.creationNotification = creationNotification;
        this.deletionNotification = deletionNotification;
        this.settings = settings;
        this.favoriteCoins = favoriteCoins;
        this.localizationService = localizationService;
    }

    public List<BotCommand> build(String locale) {
        return List.of(
                new BotCommand(mainMenu, localizationService.getCommandDescription(mainMenu, locale)),
                new BotCommand(editCoinSections, localizationService.getCommandDescription(editCoinSections, locale)),
                new BotCommand(notification, localizationService.getCommandDescription(notification, locale)),
                new BotCommand(creationNotification, localizationService.getCommandDescription(creationNotification, locale)),
                new BotCommand(deletionNotification, localizationService.getCommandDescription(deletionNotification, locale)),
                new BotCommand(settings, localizationService.getCommandDescription(settings, locale)),
                new BotCommand(favoriteCoins, localizationService.getCommandDescription(favoriteCoins, locale))
        );
    }
}