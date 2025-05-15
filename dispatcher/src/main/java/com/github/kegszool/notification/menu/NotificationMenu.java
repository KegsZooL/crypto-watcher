package com.github.kegszool.notification.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.CalledMenu;
import com.github.kegszool.notification.util.NotificationTitleBuilder;

@Component
@Scope("prototype")
public class NotificationMenu extends BaseNotificationMenu implements CalledMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;
    private final List<String> namesOfMenuSequence;

    @Autowired
    public NotificationMenu(
            NotificationTitleBuilder titleBuilder,
            @Value("${menu.notification.name}") String name,
            @Value("${menu.notification.title.ru}") String title,
            @Value("${menu.notification.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.notification.sections.ru}") String config,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton,
            @Value("${menu.notification.sequence}") List<String> namesOfMenuSequence
    ) {
        super(null, titleBuilder);
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