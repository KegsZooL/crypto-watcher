package com.github.kegszool.notification.triggered;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.user.messaging.dto.UserData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AfterTriggeredNotificationConsumer extends BaseResponseConsumer<UserData> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.get_user_data_after_triggered_notification_response}")
    public void consume(ServiceMessage<UserData> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<UserData> getDataClass() {
        return UserData.class;
    }

    //TODO: dummy
    @Override
    protected void logReceivedData(ServiceMessage<UserData> serviceMessage, String routingKey) {
    }
}
