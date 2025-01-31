package com.github.kegszool.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EntityRepository<T, ID> implements JpaRepository<T, ID> {

}