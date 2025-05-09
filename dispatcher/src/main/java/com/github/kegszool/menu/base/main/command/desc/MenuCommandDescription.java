package com.github.kegszool.menu.base.main.command.desc;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class MenuCommandDescription extends BaseMenuCommandDescription {

    public MenuCommandDescription(
            @Value("${bot.command.display_main_menu}") String command
    ) {
        super(command, Map.of(
                "ru", "Открыть главное меню",
                "en", "Open the main menu"
        ));
    }
}