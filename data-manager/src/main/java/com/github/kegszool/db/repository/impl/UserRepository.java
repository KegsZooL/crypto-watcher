package com.github.kegszool.db.repository.impl;

import com.github.kegszool.db.entity.base.User;
import com.github.kegszool.db.repository.EntityRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class UserRepository extends EntityRepository<User, Integer> {

}