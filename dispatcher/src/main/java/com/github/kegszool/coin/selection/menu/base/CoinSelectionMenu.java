package com.github.kegszool.coin.selection.menu.base;

import java.util.List;

import com.github.kegszool.coin.selection.menu.BaseCoinMenu;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.selection.util.CoinSelectionSectionBuilder;

@Component
@Scope("prototype")
public class CoinSelectionMenu extends BaseCoinMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthRow;

    public CoinSelectionMenu(
            @Value("${menu.coin_selection.name}") String name,
            @Value("${menu.coin_selection.title.ru}") String title,
            @Value("${menu.coin_selection.sections.ru}") String config,
         	@Value("${menu.coin_selection.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.action.display_edit_coin_sections_menu}") String callbackDataForFullWidthRow,
            CoinSelectionSectionBuilder sectionBuilder
    ) {
        super(sectionBuilder);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.callbackDataForFullWidthRow = callbackDataForFullWidthRow;
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
        return List.of(callbackDataForFullWidthRow);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }
}