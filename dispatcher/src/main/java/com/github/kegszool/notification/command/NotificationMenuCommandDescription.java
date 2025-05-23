package com.github.kegszool.notification.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class NotificationMenuCommandDescription extends BaseMenuCommandDescription {

    public NotificationMenuCommandDescription(
            @Value("${menu.notification.command}") String command,
            @Value("${menu.notification.command_description.ru}") String descriptionRu,
            @Value("${menu.notification.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
            )
        );
    }
}