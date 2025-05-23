package com.github.kegszool.database.entity.mapper.impl;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.mapper.EntityMapper;

@Component
public class CoinMapper extends EntityMapper<Coin, CoinDto> {

    @Override
    public Coin toEntity(CoinDto coinDto) {
        return new Coin(
            coinDto.getName()
        );
    }

    @Override
    public CoinDto toDto(Coin coin) {
        return new CoinDto(
            coin.getName()
        );
    }
}