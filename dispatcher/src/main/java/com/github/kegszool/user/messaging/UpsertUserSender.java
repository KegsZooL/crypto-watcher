package com.github.kegszool.user.messaging;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducer;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class UpsertUserSender {

    private final String routingKey;
    private final RequestProducer requestProducer;
    private final MessageUtils messageUtils;

    @Autowired
    public UpsertUserSender(@Value("${spring.rabbitmq.template.routing-key.upsert_user_request}") String routingKey,
                            RequestProducer requestProducer,
                            MessageUtils messageUtils
    ) {
        this.routingKey = routingKey;
        this.requestProducer = requestProducer;
        this.messageUtils = messageUtils;
    }

    public void send(Update update) {
        ServiceMessage<UserDto> serviceMessage = createServiceMessage(update);
        requestProducer.produce(routingKey, serviceMessage);
    }

    private ServiceMessage<UserDto> createServiceMessage(Update update) {
        Integer messageId = messageUtils.extractMessageId(update);
        String chatId = messageUtils.extractChatId(update);
        UserDto userDto = createUserDto(update);
        return new ServiceMessage<>(messageId, chatId, userDto);
    }

    private UserDto createUserDto(Update update) {
        User user = update.getMessage().getFrom();
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}