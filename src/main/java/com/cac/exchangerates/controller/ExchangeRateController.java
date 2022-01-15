package com.cac.exchangerates.controller;

import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.service.ExchangeRateConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange-rates")

public class ExchangeRateController {

    final ExchangeRateConsumerService exchangeRateConsumerService;

    @Autowired
    public ExchangeRateController(ExchangeRateConsumerService exchangeRateConsumerService) {
        this.exchangeRateConsumerService = exchangeRateConsumerService;
    }

    @GetMapping
    public ExchangeRateDto getExchangeRates() {
        return exchangeRateConsumerService.consumeExchangeRates();
    }
}
