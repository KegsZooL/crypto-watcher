package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.bot.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2 //TODO add logging
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
        if (response.isSuccess()) {
            List<FavoriteCoinDto> coins = response.getFavoriteCoins();
            StringBuilder menuSectionsConfig = new StringBuilder();
            for (int i = 0; i < coins.size(); i++) {
                FavoriteCoinDto currentFavoriteCoinDto = coins.get(i);
                CoinDto currentCoinDto = currentFavoriteCoinDto.getCoin();
                String coinName = currentCoinDto.getName();
                String coinNameWithPrefixes =  String.format("%s%s%s", COIN_PREFIX, coinName, CURRENCY_PREFIX);
                if (i == coins.size() - 1) {
                    menuSectionsConfig.append(String.format("%s:%s", coinNameWithPrefixes, coinName));
                } else {
                    menuSectionsConfig.append(String.format("%s:%s,", coinNameWithPrefixes, coinName));
                }
            }
            menuUpdaterService.updateMenuSections(COIN_SELECTIONS_MENU_NAME, menuSectionsConfig.toString());
        }
        return new HandlerResult.NoResponse();
    }
}