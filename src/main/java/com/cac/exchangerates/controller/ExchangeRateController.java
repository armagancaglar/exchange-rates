package com.cac.exchangerates.controller;

import com.cac.exchangerates.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<BigDecimal> getExchangeRateBetweenTwoCurrency(@RequestParam("from") String fromCurrency, @RequestParam("to") String toCurrency) throws IOException {
        BigDecimal rate = exchangeRateService.calculateRateBetweenCurrencies(fromCurrency.toUpperCase(), toCurrency.toUpperCase());
        return new ResponseEntity<BigDecimal>(rate, HttpStatus.OK);
    }

    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertAmountBetweenCurrencies(@RequestParam("from") String fromCurrency, @RequestParam("to") String toCurrency, @RequestParam("amount") BigDecimal amount) throws IOException {
        BigDecimal conversion =  exchangeRateService.convertAmountBetweenCurrencies(fromCurrency.toUpperCase(), toCurrency.toUpperCase(), amount);
        return new ResponseEntity<BigDecimal>(conversion, HttpStatus.OK);
    }
}
