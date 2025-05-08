package com.github.kegszool.notification.active;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.repository.impl.NotificationRepository;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.RequestExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetActiveNotificationExecutor implements RequestExecutor<String, List<NotificationDto>> {

    private final String routingKey;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Autowired
    public GetActiveNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.get_active_notification.response}") String routingKey,
            NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.routingKey = routingKey;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public ServiceMessage<List<NotificationDto>> execute(ServiceMessage<String> serviceMessage) {

        String coinName = serviceMessage.getData();
        List<Notification> activeNotifications = notificationRepository.findByCoin_NameAndIsTriggeredFalse(coinName);

        List<NotificationDto> dtoList = activeNotifications.stream()
                .map(notificationMapper::toDto)
                .toList();

        return new ServiceMessage<>(
                serviceMessage.getMessageId(),
                serviceMessage.getChatId(),
                dtoList
        );
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}