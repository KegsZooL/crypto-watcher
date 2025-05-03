package com.github.kegszool.database.entity.base;

import com.github.kegszool.messaging.dto.database_entity.Direction;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification", schema = "public")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "notification_user_id_fkey"))
    private User user;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "coin_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "notification_coin_id_fkey"))
    private Coin coin;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring;

    @Column(name = "is_triggered", nullable = false)
    private boolean isTriggered = false;

    @Column(name = "initial_price")
    private double initialPrice;

    @Column(name = "target_percentage", precision = 4, scale = 4)
    private BigDecimal targetPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "directions")
    private Direction direction;

    public Notification(
            User user,
            Integer messageId,
            Long chatId,
            Coin coin,
            boolean isRecurring,
            boolean isTriggered,
            double initialPrice,
            BigDecimal targetPercentage,
            Direction direction
    ) {
        this.user = user;
        this.messageId = messageId;
        this.chatId = chatId;
        this.isTriggered = isTriggered;
        this.initialPrice = initialPrice;
        this.coin = coin;
        this.isRecurring = isRecurring;
        this.targetPercentage = targetPercentage;
        this.direction = direction;
    }
}