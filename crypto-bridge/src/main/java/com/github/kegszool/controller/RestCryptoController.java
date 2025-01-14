package com.github.kegszool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestCryptoController {

    private final String OKX_URL = "https://www.okx.com/api/v5/market/ticker";

    @GetMapping("/ticker")
    public String getCryptoPrice(@RequestParam String instId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = OKX_URL + "?instId=" + instId;
        return restTemplate.getForObject(url, String.class);
    }
}