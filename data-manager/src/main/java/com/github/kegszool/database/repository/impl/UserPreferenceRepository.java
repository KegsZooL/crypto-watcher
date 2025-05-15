package com.github.kegszool.database.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.kegszool.database.entity.base.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> { }