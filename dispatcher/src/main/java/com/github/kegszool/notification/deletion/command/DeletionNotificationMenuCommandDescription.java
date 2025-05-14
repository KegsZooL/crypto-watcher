package com.github.kegszool.notification.deletion.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class DeletionNotificationMenuCommandDescription extends BaseMenuCommandDescription {

    public DeletionNotificationMenuCommandDescription(
            @Value("${menu.notification_deletion.command}") String command,
            @Value("${menu.notification_deletion.command_description.ru}") String descriptionRu,
            @Value("${menu.notification_deletion.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
            )
        );
    }
}