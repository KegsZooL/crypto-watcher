package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.menu.service.MenuUpdaterService;

import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UpsertUserResponseHandler extends BaseResponseHandler<UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

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

        UserData userData = response.getUserData();
        UserDto user = userData.getUser();

        if (user == null) {
            log.error("User data is missing in the upsert user response");
            return new HandlerResult.NoResponse();
        }
        logByResponseStatus(responseStatus, user);
        if (responseStatus) {
            menuUpdaterService.updateMenus(userData);
        }
        return new HandlerResult.NoResponse();
    }

    private void logByResponseStatus(boolean status, UserDto user) {
        String telegramId = user.getTelegramId().toString();
        String logMessage = status
                ? "The user's data [user_telegram_id = {}] previously existed in the database. Updating the menu fields."
                : "The user's data [user_telegram_id = {}] did not exist in the database before.";
        log.info(logMessage, telegramId);
    }
}