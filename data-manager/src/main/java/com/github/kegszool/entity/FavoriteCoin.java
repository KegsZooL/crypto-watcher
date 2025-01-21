package com.github.kegszool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "favorite_coin")
public class FavoriteCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_favorites_user_id_fkey"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "coin_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_favorites_coin_id_fkey"))
    private Coin coin;
}