package com.github.kegszool.database.repository.impl;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.repository.EntityRepository;

@Repository
public interface NotificationRepository extends EntityRepository<Notification, Integer> {
    List<Notification> findByUser_Id(int userId);
}