package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.menu.service.MenuUpdaterService;

import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UpsertUserResponseHandler extends BaseResponseHandler<UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

    @Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    @Value("${menu.coin_selection.name}")
    private String COIN_SELECTIONS_MENU_NAME;

    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public UpsertUserResponseHandler(MenuUpdaterService menuUpdaterService) {
        this.menuUpdaterService = menuUpdaterService;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return UPSERT_USER_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<UpsertUserResponse> serviceMessage) {
        UpsertUserResponse response = serviceMessage.getData();
        boolean responseStatus = response.isSuccess();
        UserDto user = response.getUser();
        if (user == null) {
            log.error("User data is missing in the upsert user response");
            return new HandlerResult.NoResponse();
        }
        logByResponseStatus(responseStatus, user);
        if (responseStatus) {
            processResponse(response);
        }
        return new HandlerResult.NoResponse();
    }

    private void processResponse(UpsertUserResponse response) {
        List<FavoriteCoinDto> favoriteCoins = response.getFavoriteCoins();
        String menuSectionsConfig = buildMenuSectionsConfig(favoriteCoins);
        menuUpdaterService.updateMenuSections(COIN_SELECTIONS_MENU_NAME, menuSectionsConfig);
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

    private void logByResponseStatus(boolean status, UserDto user) {
        String telegramId = user.getTelegramId().toString();
        String logMessage = status
                ? "The user's data [user_telegram_id = {}] previously existed in the database. Updating the menu fields."
                : "The user's data [user_telegram_id = {}] did not exist in the database before.";
        log.info(logMessage, telegramId);
    }
}