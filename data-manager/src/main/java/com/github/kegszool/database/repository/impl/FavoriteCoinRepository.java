package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class FavoriteCoinRepository extends EntityRepository<FavoriteCoin, Integer> {
}
