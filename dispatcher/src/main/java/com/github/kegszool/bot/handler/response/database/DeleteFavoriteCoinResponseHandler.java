package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.HandlerResult;
import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.menu.service.managment.MenuUpdaterService;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeleteFavoriteCoinResponseHandler extends BaseResponseHandler<UserData> {

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY;

    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public DeleteFavoriteCoinResponseHandler(MenuUpdaterService menuUpdaterService) {
        this.menuUpdaterService = menuUpdaterService;
    }

    @Override
    public HandlerResult handle(ServiceMessage<UserData> serviceMessage) {
        UserData userData = serviceMessage.getData();
        menuUpdaterService.updateMenus(userData);
        return new HandlerResult.NoResponse();
    }

    @Override
    public boolean canHandle(String routingKey) {
        return DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY.equals(routingKey);
    }
}