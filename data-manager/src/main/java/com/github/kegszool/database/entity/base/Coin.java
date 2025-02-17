package com.github.kegszool.database.entity.base;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "coin", schema = "public")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    public Coin(String name) {
        this.name = name;
    }
}