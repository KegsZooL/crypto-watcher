package com.github.kegszool.language.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.language.messaging.dto.ChangeUserLanguageRequest;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class ChangeLanguageRequestSender {

    private final String routingKey;
    private final RequestProducerService requestProducer;

    @Autowired
    public ChangeLanguageRequestSender(
            @Value("${spring.rabbitmq.template.routing-key.change_language_request}") String routingKey,
            RequestProducerService requestProducer
    ) {
        this.routingKey = routingKey;
        this.requestProducer = requestProducer;
    }

    public void send(CallbackQuery callbackQuery, String language) {

        User user = callbackQuery.getFrom();
        var msg = callbackQuery.getMessage();

        String chatId = msg.getChatId().toString();
        Integer messageId = msg.getMessageId();

        UserDto userDto = new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );

        ChangeUserLanguageRequest request = new ChangeUserLanguageRequest(userDto, language);

        ServiceMessage<ChangeUserLanguageRequest> serviceMessage = new ServiceMessage<>(
                messageId, chatId, request
        );
        requestProducer.produce(routingKey, serviceMessage);
    }
}