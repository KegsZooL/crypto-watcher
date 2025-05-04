package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.request.impl.GetActiveNotificationExecutor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class GetActiveNotificationConsumer extends BaseRequestConsumer<String, GetActiveNotificationExecutor> {

    @Autowired
    public GetActiveNotificationConsumer(
            ResponseProducerService responseProducer,
            GetActiveNotificationExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.get_active_notification.request}")
    public void listen(
            ServiceMessage<String> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}