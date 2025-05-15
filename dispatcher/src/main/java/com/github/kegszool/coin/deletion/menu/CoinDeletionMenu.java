package com.github.kegszool.coin.deletion.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.selection.menu.BaseCoinMenu;
import com.github.kegszool.coin.deletion.util.CoinDeletionSectionBuilder;

@Component
@Scope("prototype")
public class CoinDeletionMenu extends BaseCoinMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;

    public CoinDeletionMenu(
            @Value("${menu.coin_deletion_menu.name}") String name,
         	@Value("${menu.coin_deletion_menu.title.ru}") String title,
     		@Value("${menu.coin_deletion_menu.sections.ru}") String config,
            @Value("${menu.coin_deletion_menu.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.action.delete_selected}") String callbackDataForFullWidthButton,
            CoinDeletionSectionBuilder sectionBuilder
    ) {
        super(sectionBuilder);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.callbackDataForFullWidthButton = callbackDataForFullWidthButton;
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
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }
}