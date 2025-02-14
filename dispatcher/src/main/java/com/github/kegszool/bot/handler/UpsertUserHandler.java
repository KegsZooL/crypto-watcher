package com.github.kegszool.bot.handler;

import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.utils.MessageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.extern.log4j.Log4j2;

@Log4j2 //TODO refactoring
@Component
public class UpsertUserHandler {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_request}")
    private String UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY;

    private final RequestProducerService requestProducer;
    private final MessageUtils messageUtils;

    private boolean isVerified = false;

    @Autowired
    public UpsertUserHandler(RequestProducerService requestProducer, MessageUtils messageUtils) {
        this.requestProducer = requestProducer;
        this.messageUtils = messageUtils;
    }

    public void handle(Update update) {
        if (!isVerified) {
            isVerified = true;
            produceRequest(update);
        }
    }

    private void produceRequest(Update update) {
        Integer messageId = messageUtils.extractMessageId(update);
        String chatId = messageUtils.extractChatId(update);

        UserDto userDto = createUserDto(update);
        var serviceMessage = new ServiceMessage<>(
                messageId, chatId, userDto
        );
        requestProducer.produce(UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY, serviceMessage);
    }

    private UserDto createUserDto(Update update) {
        User user = update.getMessage().getFrom();

        Long telegramId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        log.info("The user_DTO was created. User=" +
                        "[telegram_id = \"{}\", firstName = \"{}\", lastName = \"{}\"",
                telegramId, firstName, lastName);

        return new UserDto(
                telegramId, firstName, lastName
        );
    }
}