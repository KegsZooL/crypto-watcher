package com.github.kegszool.bot.handler;

import com.github.kegszool.messaging.dto.database.impl.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j2 //TODO: add logging
public class UpsertUserHandler {

    @Value("${spring.rabbitmq.template.routing-key.re}")
    private String UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY;

    private final boolean isVerified = false;

    private final RequestProducerService requestProducer;


    @Autowired
    public UpsertUserHandler(RequestProducerService requestProducer) {
        this.requestProducer = requestProducer;
    }

    public void handle(Update update) {
        if(!isVerified) {
            ServiceMessage<UserDto> serviceMessage = new ServiceMessage<>(

            );
            requestProducer.produce(UPSET_USER_REQUEST_TO_DATABASE_ROUTING_KEY, );
        }
    }
}