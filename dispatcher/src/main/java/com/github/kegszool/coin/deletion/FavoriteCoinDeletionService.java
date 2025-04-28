package com.github.kegszool.coin.deletion;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.FavoriteCoinMenu;
import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.coin.selection.state.MenuSelectionBuffer;
import com.github.kegszool.coin.deletion.util.CoinDeletionUserDataFactory;

import com.github.kegszool.menu.base.Menu;
import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuUpdaterService;

import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.UserDataFactory;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class FavoriteCoinDeletionService {

    private final MenuUpdaterService menuUpdater;
    private final MenuSelectionBuffer menuSelectionBuffer;
    private final MenuRegistry menuRegistry;

    private final RequestProducerService requestProducerService;
    private final CoinDeletionUserDataFactory coinDeletionUserDataFactory;
    private final UserDataFactory userDataFactory;

    private final String coinDeletionMenuName;
    private final String coinSelectionMenuName;
    private final String routingKey;

    @Autowired
    public FavoriteCoinDeletionService(
            MenuUpdaterService menuUpdater,
            MenuSelectionBuffer menuSelectionBuffer,
            MenuRegistry menuRegistry,

            RequestProducerService requestProducerService,
            CoinDeletionUserDataFactory coinDeletionUserDataFactory,
            UserDataFactory userDataFactory,
     		@Value("${menu.coin_deletion_menu.name}") String coinDeletionMenuName,
     		@Value("${menu.coin_selection.name}") String coinSelectionMenuName,
            @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_request}") String routingKey
    ) {
        this.menuUpdater = menuUpdater;
        this.menuSelectionBuffer = menuSelectionBuffer;
        this.menuRegistry = menuRegistry;

        this.requestProducerService = requestProducerService;
        this.coinDeletionUserDataFactory = coinDeletionUserDataFactory;
        this.userDataFactory = userDataFactory;

        this.coinDeletionMenuName = coinDeletionMenuName;
        this.coinSelectionMenuName = coinSelectionMenuName;
        this.routingKey = routingKey;
    }

    public void deleteSelectedCoins(CallbackQuery callbackQuery) {
        List<InlineKeyboardButton> selectedButtons = menuSelectionBuffer.getSelected(coinDeletionMenuName);
        if (selectedButtons.isEmpty()) return;

        UserData deletionUserData = coinDeletionUserDataFactory.createFromSelected(callbackQuery, selectedButtons);
        menuSelectionBuffer.removeSelected(coinDeletionMenuName);
        produceDeleteRequest(callbackQuery, deletionUserData);

        Menu menu = menuRegistry.getMenu(coinSelectionMenuName);
        if (!(menu instanceof FavoriteCoinMenu favoriteCoinMenu)) return;

        UserDto userDto = deletionUserData.getUser();
        List<FavoriteCoinDto> allCoins = favoriteCoinMenu.getAllFavoriteCoins(userDto);

        Set<String> coinsToDelete = deletionUserData.getFavoriteCoins().stream()
                .map(fc -> fc.getCoin().getName())
                .collect(Collectors.toSet());

        List<FavoriteCoinDto> remainingCoins = new ArrayList<>(allCoins.size());
        for (FavoriteCoinDto coin : allCoins) {
            String coinName = coin.getCoin().getName();
            if (!coinsToDelete.contains(coinName)) {
                remainingCoins.add(coin);
            }
        }
        UserData data = userDataFactory.create(userDto, remainingCoins, Collections.emptyList());
        menuUpdater.updateMenus(data);
    }

    private void produceDeleteRequest(CallbackQuery callbackQuery, UserData userData) {
        ServiceMessage<UserData> serviceMessage = new ServiceMessage<>(
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getId(),
                userData
        );
        requestProducerService.produce(routingKey, serviceMessage);
    }
}