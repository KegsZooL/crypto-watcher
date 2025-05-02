package com.github.kegszool.request.impl.notificaiton;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.producer.ResponseProducer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class NotificationMessageSender {

    private final ResponseProducer responseProducer;

    public NotificationMessageSender(ResponseProducer responseProducer) {
        this.responseProducer = responseProducer;
    }

    public void send(ServiceMessage<NotificationDto> serviceMessage) {


//        responseProducer.produce();
    }
}