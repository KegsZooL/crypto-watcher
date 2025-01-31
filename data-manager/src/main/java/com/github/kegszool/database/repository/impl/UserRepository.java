package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class UserRepository extends EntityRepository<User, Integer> {

}