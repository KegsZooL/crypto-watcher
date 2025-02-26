package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.impl.user_data_dependent.UserDataDependentBaseMenu;
import com.github.kegszool.bot.menu.service.managment.MenuUpdaterService;
import com.github.kegszool.bot.menu.service.section.builder.impl.CoinDeletionSectionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoinDeletionMenu extends UserDataDependentBaseMenu {

    @Value("${menu.coin_deletion_menu.sections}")
    private String SECTIONS_CONFIG;

    @Value("${menu.coin_deletion_menu.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.coin_deletion_menu.title}")
    private String TITLE;

    @Value("${menu.coin_deletion_menu.name}")
    private String NAME;

    @Value("${menu.action.delete_selected}")
    private String DELETE_SELECTED_CALLBACK_DATA;

    @Value("${menu.coin_deletion_menu.hint}")
    private String HINT;

    @Autowired
    public CoinDeletionMenu(
            MenuUpdaterService menuUpdaterService,
            CoinDeletionSectionBuilder sectionBuilder
    ) {
        super(menuUpdaterService, sectionBuilder);
    }

    @Override
    protected String getSectionsConfig() {
        return SECTIONS_CONFIG;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return List.of(DELETE_SELECTED_CALLBACK_DATA);
    }

    @Override
    public String getTitle() {
        return TITLE;

    }
    public String getHint() {
        return HINT;
    }

    @Override
    public String getName() {
        return NAME;
    }
}