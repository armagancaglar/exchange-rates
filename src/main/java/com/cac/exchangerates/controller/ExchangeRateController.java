package com.cac.exchangerates.controller;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionResponseDto;
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
    public ResponseEntity<BigDecimal> getExchangeRateBetweenTwoCurrency(@RequestParam("from") CurrencyEnum baseCurrency, @RequestParam("to") CurrencyEnum targetCurrency) throws IOException {
        BigDecimal rate = exchangeRateService.calculateRateBetweenCurrencies(baseCurrency, targetCurrency);
        return new ResponseEntity<BigDecimal>(rate, HttpStatus.OK);
    }

    @GetMapping("/convert")
    public ResponseEntity<ConversionResponseDto> convertAmountBetweenCurrencies(@RequestParam("from") CurrencyEnum baseCurrency, @RequestParam("to") CurrencyEnum targetCurrency, @RequestParam("amount") BigDecimal amount) throws IOException {
        ConversionResponseDto conversion =  exchangeRateService.convertAmountBetweenCurrencies(baseCurrency, targetCurrency, amount);
        return new ResponseEntity<ConversionResponseDto>(conversion, HttpStatus.OK);
    }
}
