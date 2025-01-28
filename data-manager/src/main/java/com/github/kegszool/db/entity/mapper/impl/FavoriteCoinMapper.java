package com.github.kegszool.db.entity.mapper.impl;

import com.github.kegszool.db.entity.base.FavoriteCoin;
import com.github.kegszool.db.entity.mapper.EntityMapper;
import com.github.kegszool.db.entity.dto.impl.FavoriteCoinDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteCoinMapper extends EntityMapper<FavoriteCoin, FavoriteCoinDto> {

    private final UserMapper userMapper;
    private final CoinMapper coinMapper;

    @Autowired
    public FavoriteCoinMapper(UserMapper userMapper, CoinMapper coinMapper) {
        this.userMapper = userMapper;
        this.coinMapper = coinMapper;
    }

    @Override
    public FavoriteCoin toEntity(FavoriteCoinDto favoriteCoinDto) {
        return new FavoriteCoin(
            favoriteCoinDto.getId(),
            userMapper.toEntity(favoriteCoinDto.getUser()),
            coinMapper.toEntity(favoriteCoinDto.getCoin())
        );
    }

    @Override
    public FavoriteCoinDto toDto(FavoriteCoin favoriteCoin) {
        return new FavoriteCoinDto(
            favoriteCoin.getId(),
            userMapper.toDto(favoriteCoin.getUser()),
            coinMapper.toDto(favoriteCoin.getCoin())
        );
    }
}
