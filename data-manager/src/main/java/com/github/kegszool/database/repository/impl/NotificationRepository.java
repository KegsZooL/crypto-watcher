package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class NotificationRepository extends EntityRepository<Notification, Integer> {
}
