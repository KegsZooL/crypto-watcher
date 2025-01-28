package com.github.kegszool.db.entity.mapper.impl;

import com.github.kegszool.db.entity.base.Notification;
import com.github.kegszool.db.entity.dto.impl.NotificationDto;
import com.github.kegszool.db.entity.mapper.EntityMapper;
import org.springframework.stereotype.Component;

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
                notificationDto.getId(),
                userMapper.toEntity(notificationDto.getUser()),
                coinMapper.toEntity(notificationDto.getCoin()),
                notificationDto.isActive(),
                notificationDto.isRecurring(),
                notificationDto.getTargetPrice(),
                notificationDto.getTargetPercentage(),
                notificationDto.getDirection()
        );
    }

    @Override
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                userMapper.toDto(notification.getUser()),
                coinMapper.toDto(notification.getCoin()),
                notification.isActive(),
                notification.isRecurring(),
                notification.getTargetPrice(),
                notification.getTargetPercentage(),
                notification.getDirection()
        );
    }
}