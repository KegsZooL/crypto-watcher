package com.github.kegszool.bot.menu.impl.user_data_dependent;

import com.github.kegszool.bot.menu.service.managment.MenuUpdaterService;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CoinSelectionMenu extends UserDataDependentBaseMenu {

    @Value("${menu.coin_selection.name}")
    private String NAME;

    @Value("${menu.coin_selection.title}")
    private String TITLE;

    @Value("${menu.coin_selection.sections}")
    private String MENU_SECTIONS_CONFIG;

    @Value("${menu.coin_selection.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.action.open_edit_coin_sections_menu}")
    private String EDIT_COIN_SECTIONS_CALLBACK_DATA;

    @Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    @Autowired
    public CoinSelectionMenu(MenuUpdaterService menuUpdaterService) {
        super(menuUpdaterService);
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
    @Override
    public void updateMenu(UserData userData) {
        processUpdate(userData);
    }

    private void processUpdate(UserData userData) {
        List<FavoriteCoinDto> favoriteCoins = userData.getFavoriteCoins();
        String menuSectionsConfig = buildMenuSectionsConfig(favoriteCoins);
        updateSections(menuSectionsConfig, true);
    }

    private String buildMenuSectionsConfig(List<FavoriteCoinDto> favoriteCoins) {
        return favoriteCoins.stream()
                .map(favoriteCoin -> {
                    CoinDto coin = favoriteCoin.getCoin();
                    String coinName = coin.getName();
                    String coinNamWithPrefixes = COIN_PREFIX + coinName + CURRENCY_PREFIX;
                    return String.format("%s:%s", coinNamWithPrefixes, coinName);
                }).collect(Collectors.joining(","));
    }
}