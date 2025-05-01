package com.github.kegszool.database.entity.mapper.impl;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.mapper.EntityMapper;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

@Component
public class NotificationMapper extends EntityMapper<Notification, NotificationDto> {

    private final UserMapper userMapper;
    private final CoinMapper coinMapper;

    public NotificationMapper(UserMapper userMapper, CoinMapper coinMapper) {
        this.userMapper = userMapper;
        this.coinMapper = coinMapper;
    }

    @Override
    public Notification toEntity(NotificationDto notificationDto) {
        return new Notification(
                userMapper.toEntity(notificationDto.getUser()),
                coinMapper.toEntity(notificationDto.getCoin()),
                notificationDto.isActive(),
                notificationDto.isRecurring(),
                notificationDto.getTargetPercentage(),
                notificationDto.getDirection()
        );
    }

    @Override
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                userMapper.toDto(notification.getUser()),
                coinMapper.toDto(notification.getCoin()),
                notification.isActive(),
                notification.isRecurring(),
                notification.getTargetPercentage(),
                notification.getDirection()
        );
    }
}