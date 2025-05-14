package com.github.kegszool.notification.deletion.menu;

import java.util.List;

import com.github.kegszool.menu.CalledMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.notification.menu.BaseNotificationMenu;
import com.github.kegszool.notification.deletion.util.NotificationDeletionSectionBuilder;

@Component
public class NotificationDeletionMenu extends BaseNotificationMenu implements CalledMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;
    private final List<String> namesOfMenuSequence;

    public NotificationDeletionMenu(
            NotificationDeletionSectionBuilder sectionBuilder,
            @Value("${menu.notification_deletion.name}") String name,
            @Value("${menu.notification_deletion.title.ru}") String title,
            @Value("${menu.notification_deletion.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.notification_deletion.sections.ru}") String config,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton,
            @Value("${menu.notification_deletion.sequence}") List<String> namesOfMenuSequence
    ) {
        super(sectionBuilder, null);
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
    public List<String> getMenuSequence() {
        return namesOfMenuSequence;
    }
}