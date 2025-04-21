package com.github.kegszool.coin.deletion.handler;

import com.github.kegszool.messaging.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.user.dto.UserData;
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