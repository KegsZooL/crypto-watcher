package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends EntityRepository<Coin, Integer> {
}