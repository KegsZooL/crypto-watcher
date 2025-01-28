package com.github.kegszool.db.repository.impl;

import com.github.kegszool.db.entity.base.FavoriteCoin;
import com.github.kegszool.db.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class FavoriteCoinRepository extends EntityRepository<FavoriteCoin, Integer> {
}
