package com.github.kegszool.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository<E, ID> extends JpaRepository<E, ID> { }