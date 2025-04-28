package com.github.kegszool.database.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_preferences", schema = "public")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "interface_language", length = 2, nullable = false)
    private String interfaceLanguage = "ru";

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_preferences_user_id_fkey"))
    private User user;

    public UserPreference(User user, String interfaceLanguage) {
        this.user = user;
        this.interfaceLanguage = interfaceLanguage;
    }
}