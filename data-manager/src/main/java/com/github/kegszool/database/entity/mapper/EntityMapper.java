package com.github.kegszool.database.entity.mapper;

import org.springframework.stereotype.Component;

@Component
public abstract class EntityMapper<E, Dto> {

    public abstract E toEntity(Dto entityDto);
    public abstract Dto toDto(E entity);
}