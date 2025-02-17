package com.github.kegszool.database.entity.base;

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

    @ManyToOne
    @JoinColumn(name = "coin_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "notification_coin_id_fkey"))
    private Coin coin;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring;

    @Column(name = "target_price", precision = 15, scale = 9)
    private BigDecimal targetPrice;

    @Column(name = "target_percentage", precision = 4, scale = 2)
    private BigDecimal targetPercentage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "directions")
    private Direction direction;

    public Notification(
            User user,
            Coin coin,
            boolean isActive, boolean isRecurring,
            BigDecimal targetPrice, BigDecimal targetPercentage,
            Direction direction
    ) {
        this.user = user;
        this.coin = coin;
        this.isActive = isActive;
        this.isRecurring = isRecurring;
        this.targetPrice = targetPrice;
        this.targetPercentage = targetPercentage;
        this.direction = direction;
    }
}