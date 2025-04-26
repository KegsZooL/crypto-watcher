package com.github.kegszool.coin.selection.menu;

import com.github.kegszool.coin.FavoriteCoinMenu;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.coin.selection.CoinSelectionSectionBuilder;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class CoinSelectionMenu extends FavoriteCoinMenu {

    @Value("${menu.coin_selection.name}")
    private String NAME;

    @Value("${menu.coin_selection.title}")
    private String TITLE;

    @Value("${menu.coin_selection.sections}")
    private String MENU_SECTIONS_CONFIG;

    @Value("${menu.coin_selection.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.action.display_edit_coin_sections_menu}")
    private String EDIT_COIN_SECTIONS_CALLBACK_DATA;

    @Autowired
    public CoinSelectionMenu(
            MenuUpdaterService menuUpdaterService,
            CoinSelectionSectionBuilder sectionBuilder
    ) {
        super(menuUpdaterService, sectionBuilder);
    }

    @Override
    protected String getSectionsConfig() {
        return MENU_SECTIONS_CONFIG;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return List.of(EDIT_COIN_SECTIONS_CALLBACK_DATA);
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