package com.github.kegszool.coin.addition.messaging.add.response;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.service.MenuUpdaterService;

@Component
public class CoinAdditionResponseHandler extends BaseResponseHandler<UserData> {

    private final String routingKey;
    private final MenuUpdaterService menuUpdater;

    @Autowired
    public CoinAdditionResponseHandler
            (@Value("${spring.rabbitmq.template.routing-key.add_coin_response}") String routingKey,
             MenuUpdaterService menuUpdater
    ) {
        this.routingKey = routingKey;
        this.menuUpdater = menuUpdater;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<UserData> serviceMessage) {
        menuUpdater.updateMenus(serviceMessage.getData());
        return new HandlerResult.NoResponse();
    }
}