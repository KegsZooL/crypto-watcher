package com.github.kegszool.database.repository.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.repository.EntityRepository;

@Repository
public interface CoinRepository extends EntityRepository<Coin, Integer> {
    Optional<Coin> findByName(String name);
}