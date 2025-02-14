package com.github.kegszool.database.entity.base;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public User(Long telegramId, String firstName, String lastName) {
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}