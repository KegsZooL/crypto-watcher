package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends EntityRepository<Notification, Integer> {
    List<Notification> findByUser_Id(int userId);
}