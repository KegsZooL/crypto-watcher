package com.github.kegszool.coin.selection.menu;

import com.github.kegszool.menu.base.BaseMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Collections;

@Component
public class EditCoinSectionsMenu extends BaseMenu {

    @Value("${menu.edit_coin_sections.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.edit_coin_sections.sections}")
    private String SECTIONS_CONFIG;

    @Value("${menu.edit_coin_sections.title}")
    private String TITLE;

    @Value("${menu.edit_coin_sections.name}")
    private String NAME;

    @Override
    protected List<String> getFullWidthSections() {
        return Collections.emptyList();
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    protected String getSectionsConfig() {
        return SECTIONS_CONFIG;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}