package com.github.kegszool.notification.creation.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.menu.CalledMenu;
import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.user.messaging.dto.UserData;

@Component
public class NotificationCreationMenu extends BaseMenu implements CalledMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;
    private final List<String> namesOfMenuSequence;

    public NotificationCreationMenu(
            @Value("${menu.notification_creation.name}") String name,
            @Value("${menu.notification_creation.title.ru}") String title,
            @Value("${menu.notification_creation.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.notification_creation.sections.ru}") String config,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton,
            @Value("${menu.notification_creation.sequence}") List<String> namesOfMenuSequence
    ) {
        super(null, null);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.callbackDataForFullWidthButton = callbackDataForFullWidthButton;
        this.namesOfMenuSequence = namesOfMenuSequence;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    protected String getSectionsConfig() {
        return config;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return maxButtonsPerRow;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return List.of(callbackDataForFullWidthButton);
    }

    @Override
    public boolean hasDataChanged(UserData userData, String chatId) {
        return isLocaleChanged(userData, chatId);
    }

    @Override
    public List<String> getMenuSequence() {
        return namesOfMenuSequence;
    }
}