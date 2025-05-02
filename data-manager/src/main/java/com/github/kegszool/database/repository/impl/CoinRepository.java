package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinRepository extends EntityRepository<Coin, Integer> {

    @Query("SELECT c FROM Coin c WHERE c.name IN :names")
    List<Coin> findByNameIn(@Param("names") List<String> names);

    Optional<Coin> findByName(String name);
}