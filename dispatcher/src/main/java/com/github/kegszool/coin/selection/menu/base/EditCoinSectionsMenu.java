package com.github.kegszool.coin.selection.menu.base;

import java.util.List;
import java.util.Collections;

import com.github.kegszool.menu.CalledMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.user.messaging.dto.UserData;

@Component
public class EditCoinSectionsMenu extends BaseMenu implements CalledMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;
    private final List<String> namesOfMenuSequence;

    public EditCoinSectionsMenu(
            @Value("${menu.edit_coin_sections.name}") String name,
            @Value("${menu.edit_coin_sections.title.ru}") String title,
            @Value("${menu.edit_coin_sections.sections.ru}") String config,
            @Value("${menu.edit_coin_sections.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.edit_coin_sections.sequence}") List<String> namesOfMenuSequence
    ) {
        super(null, null);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.namesOfMenuSequence = namesOfMenuSequence;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return Collections.emptyList();
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return maxButtonsPerRow;
    }

    @Override
    protected String getSectionsConfig() {
        return config;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
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