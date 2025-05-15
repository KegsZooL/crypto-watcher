package com.github.kegszool.database.repository.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.repository.EntityRepository;

@Repository
public interface UserRepository extends EntityRepository<User, Integer> {
    Optional<User> findByTelegramId(Long telegramId);
}