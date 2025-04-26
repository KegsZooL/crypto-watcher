package com.github.kegszool.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.messaging.util.MessageUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class UpsertUserHandler {

    private static final String LOG_MESSAGE =
            "The User Dto [telegram_id = \"{}\", firstName = \"{}\", lastName = \"{}\"] was created to get data from the database";

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_request}")
    private String UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY;

    private final RequestProducerService requestProducer;
    private final MessageUtils messageUtils;

    private final AtomicBoolean isVerified = new AtomicBoolean(false);

    @Autowired
    public UpsertUserHandler(RequestProducerService requestProducer, MessageUtils messageUtils) {
        this.requestProducer = requestProducer;
        this.messageUtils = messageUtils;
    }

    public void handle(Update update) {
        if (isVerified.compareAndSet(false, true)) {
            produceRequest(update);
        }
    }

    private void produceRequest(Update update) {
        ServiceMessage<UserDto> serviceMessage = createServiceMessage(update);
        requestProducer.produce(UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY, serviceMessage);
    }

    private ServiceMessage<UserDto> createServiceMessage(Update update) {
        Integer messageId = messageUtils.extractMessageId(update);
        String chatId = messageUtils.extractChatId(update);
        UserDto userDto = createUserDto(update);
        return new ServiceMessage<>(messageId, chatId, userDto);
    }

    private UserDto createUserDto(Update update) {
        User user = update.getMessage().getFrom();

        Long telegramId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        log.info(LOG_MESSAGE, telegramId, firstName, lastName);
        return new UserDto(
                telegramId, firstName, lastName
        );
    }
}