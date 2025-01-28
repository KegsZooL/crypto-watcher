package com.github.kegszool.db.repository.impl;

import com.github.kegszool.db.entity.base.Notification;
import com.github.kegszool.db.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class NotificationRepository extends EntityRepository<Notification, Integer> {
}
