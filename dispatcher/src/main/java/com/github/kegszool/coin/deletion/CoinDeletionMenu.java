package com.github.kegszool.coin.deletion;

import java.util.List;

import com.github.kegszool.LocalizationService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.FavoriteCoinMenu;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.coin.deletion.util.CoinDeletionSectionBuilder;

@Component
public class CoinDeletionMenu extends FavoriteCoinMenu {

    @Value("${menu.coin_deletion_menu.sections.ru}")
    private String SECTIONS_CONFIG;

    @Value("${menu.coin_deletion_menu.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.coin_deletion_menu.title.ru}")
    private String TITLE;

    @Value("${menu.coin_deletion_menu.name}")
    private String NAME;

    @Value("${menu.action.delete_selected}")
    private String DELETE_SELECTED_CALLBACK_DATA;

    @Autowired
    public CoinDeletionMenu(
            MenuUpdaterService menuUpdaterService,
            CoinDeletionSectionBuilder sectionBuilder,
            LocalizationService localizationService
    ) {
        super(menuUpdaterService, sectionBuilder, localizationService);
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

    @Override
    public String getName() {
        return NAME;
    }
}