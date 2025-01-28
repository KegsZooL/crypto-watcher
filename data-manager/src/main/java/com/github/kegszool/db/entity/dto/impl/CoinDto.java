package com.github.kegszool.db.entity.dto.impl;

import com.github.kegszool.db.entity.dto.BaseEntityDto;

public class CoinDto extends BaseEntityDto {

    private String name;

    public CoinDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}