package com.github.kegszool.database.entity.mapper.impl;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.database.entity.dto.impl.CoinDto;
import org.springframework.stereotype.Component;

@Component
public class CoinMapper extends EntityMapper<Coin, CoinDto> {

    @Override
    public Coin toEntity(CoinDto coinDto) {
        return new Coin(
            coinDto.getId(),
            coinDto.getName()
        );
    }

    @Override
    public CoinDto toDto(Coin coin) {
        return new CoinDto(
            coin.getId(),
            coin.getName()
        );
    }
}