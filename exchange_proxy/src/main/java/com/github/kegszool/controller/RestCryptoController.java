package com.github.kegszool.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/api")
public class RestCryptoController {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getResponse(@RequestParam String url) {
        var response = restTemplate.getForObject(url, String.class);
        log.info("GET request executed to URL: {}\n\t\t Response: {}",
                url, response);
        return response;
    }
}