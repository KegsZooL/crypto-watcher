package com.github.kegszool.db.repository.impl;

import com.github.kegszool.db.entity.base.Coin;
import com.github.kegszool.db.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class CoinRepository extends EntityRepository<Coin, Integer> {
}
