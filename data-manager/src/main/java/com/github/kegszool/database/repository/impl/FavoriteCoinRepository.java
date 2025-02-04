package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteCoinRepository extends EntityRepository<FavoriteCoin, Integer> {
    List<FavoriteCoin> findByUser_Id(int userId);
}