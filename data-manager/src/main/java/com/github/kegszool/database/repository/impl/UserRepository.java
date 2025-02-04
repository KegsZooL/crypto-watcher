package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends EntityRepository<User, Integer> {
    Optional<User> findByTelegramId(Long telegramId);
}