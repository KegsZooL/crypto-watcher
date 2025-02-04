package com.github.kegszool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.github.kegszool.database.repository.impl")
@SpringBootApplication
public class DataManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataManagementApplication.class);
    }
}