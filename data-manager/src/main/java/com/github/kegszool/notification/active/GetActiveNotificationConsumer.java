package com.github.kegszool.notification.active;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

@Component
public class GetActiveNotificationConsumer extends BaseRequestConsumer<String, GetActiveNotificationExecutor> {

    @Autowired
    public GetActiveNotificationConsumer(
            ProducerService responseProducer,
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