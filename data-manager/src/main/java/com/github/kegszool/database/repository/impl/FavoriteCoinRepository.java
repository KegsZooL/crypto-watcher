package com.github.kegszool.database.repository.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.repository.EntityRepository;

@Repository
public interface FavoriteCoinRepository extends EntityRepository<FavoriteCoin, Integer> {
    List<FavoriteCoin> findByUser_Id(int userId);
}