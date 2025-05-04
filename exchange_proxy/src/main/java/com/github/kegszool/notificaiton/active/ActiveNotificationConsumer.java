package com.github.kegszool.notificaiton.active;

import java.util.List;
import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

@Component
public class ActiveNotificationConsumer extends BaseRequestConsumer<List<NotificationDto>, ActiveNotificationExecutor> {

    public ActiveNotificationConsumer(
            ProducerService responseProducer,
            ActiveNotificationExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.get_active_notification_response}")
    public void listen(
            ServiceMessage<List<NotificationDto>> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}