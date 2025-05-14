package com.github.kegszool.notification.creation.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class CreationNotificationMenuCommandDescription extends BaseMenuCommandDescription {

    public CreationNotificationMenuCommandDescription(
            @Value("${menu.notification_creation.display_menu_command}") String command,
            @Value("${menu.notification_creation.command_description.ru}") String descriptionRu,
            @Value("${menu.notification_creation.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
            )
        );
    }
}