package com.github.kegszool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "notification")
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
    private float targetPrice;

    @Column(name = "target_percentage", precision = 4, scale = 2)
    private float targetPercentage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "directions")
    private Direction direction;
}