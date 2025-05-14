package com.github.kegszool.menu.base.main.command.desc;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class MainMenuCommandDescription extends BaseMenuCommandDescription {

    public MainMenuCommandDescription(
            @Value("${menu.main.command}") String command,
            @Value("${menu.main.command_description.ru}") String descriptionRu,
            @Value("${menu.main.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
                )
        );
    }
}