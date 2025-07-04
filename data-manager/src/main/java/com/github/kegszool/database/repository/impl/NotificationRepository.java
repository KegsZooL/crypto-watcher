package com.github.kegszool.database.repository.impl;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.stereotype.Repository;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.repository.EntityRepository;
import com.github.kegszool.messaging.dto.database_entity.Direction;

@Repository
public interface NotificationRepository extends EntityRepository<Notification, Integer> {

    List<Notification> findByIsTriggeredFalse();
    List<Notification> findByCoin_NameAndIsTriggeredFalse(String coinName);
    List<Notification> findByUser_IdAndIsTriggeredFalse(int userId);

    List<Notification> findByUser_IdAndCoin_IdAndInitialPriceAndTargetPercentageAndDirectionAndIsRecurring(
            int userId,
            int coinId,
            double initialPrice,
            BigDecimal targetPercentage,
            Direction direction,
            boolean isRecurring
    );

    List<Notification> findByUser_IdAndCoin_IdAndInitialPriceAndTargetPercentageAndDirectionAndIsRecurringAndIsTriggered(
            int userId,
            int coinId,
            double initialPrice,
            BigDecimal targetPercentage,
            Direction direction,
            boolean isRecurring,
            boolean isTriggered
    );

    Optional<Notification> findByUser_IdAndMessageIdAndChatIdAndCoin_IdAndIsRecurringAndInitialPriceAndTargetPercentageAndDirection(
            int userId,
            Integer messageId,
            Long chatId,
            int coinId,
            boolean isRecurring,
            double initialPrice,
            BigDecimal targetPercentage,
            Direction direction
    );

    List<Notification> findByUser_IdAndCoin_IdAndIsRecurringAndIsTriggered(
            int userId, int coinId, boolean isRecurring, boolean isTriggered
    );
}