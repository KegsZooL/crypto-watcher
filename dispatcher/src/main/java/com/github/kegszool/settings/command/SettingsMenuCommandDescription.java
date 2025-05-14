package com.github.kegszool.settings.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class SettingsMenuCommandDescription extends BaseMenuCommandDescription {

    public SettingsMenuCommandDescription(
            @Value("${menu.settings.command}") String command,
            @Value("${menu.settings.command_description.ru}") String descriptionRu,
            @Value("${menu.settings.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
        	)
        );
    }
}