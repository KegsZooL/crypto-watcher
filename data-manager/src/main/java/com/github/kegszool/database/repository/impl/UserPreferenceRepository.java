package com.github.kegszool.database.repository.impl;

import com.github.kegszool.database.entity.base.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
}