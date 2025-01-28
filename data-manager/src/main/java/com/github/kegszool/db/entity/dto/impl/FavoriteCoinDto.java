package com.github.kegszool.db.entity.dto.impl;

import com.github.kegszool.db.entity.dto.BaseEntityDto;

public class FavoriteCoinDto extends BaseEntityDto {

    private UserDto user;
    private CoinDto coin;

    public FavoriteCoinDto(int id, UserDto user, CoinDto coin) {
        this.id = id;
        this.user = user;
        this.coin = coin;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    public void setCoin(CoinDto coin) {
        this.coin = coin;
    }

    public CoinDto getCoin() {
        return coin;
    }
}
