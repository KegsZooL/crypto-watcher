package com.github.kegszool.database.entity.base;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "favorite_coin", schema = "public")
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


    public FavoriteCoin(User user, Coin coin) {
        this.user = user;
        this.coin = coin;
    }
}