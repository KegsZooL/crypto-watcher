package com.github.kegszool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ExchangeProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeProxyApplication.class, args);
    }
}