package com.github.kegszool.coin.deletion.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.menu.service.MenuUpdaterService;

@Component
public class DeleteFavoriteCoinResponseHandler extends BaseResponseHandler<UserData> {

    private final MenuUpdaterService menuUpdaterService;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY;

    @Autowired
    public DeleteFavoriteCoinResponseHandler(MenuUpdaterService menuUpdaterService) {
        this.menuUpdaterService = menuUpdaterService;
    }

    @Override
    public HandlerResult handle(ServiceMessage<UserData> serviceMessage) {
        menuUpdaterService.updateMenus(serviceMessage.getData(), serviceMessage.getChatId());
        return new HandlerResult.NoResponse();
    }

    @Override
    public boolean canHandle(String routingKey) {
        return DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY.equals(routingKey);
    }
}