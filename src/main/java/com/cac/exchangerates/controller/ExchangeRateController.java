package com.cac.exchangerates.controller;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.RestResourceResponse;
import com.cac.exchangerates.service.ExchangeRateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class ExchangeRateController extends AbstractController{
    Logger logger = LogManager.getLogger();

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchange-rate")
    public RestResourceResponse getExchangeRateBetweenTwoCurrency(@RequestParam("from") CurrencyEnum baseCurrency, @RequestParam("to") CurrencyEnum targetCurrency) {
        try {
            BigDecimal rate = exchangeRateService.calculateRateBetweenCurrencies(baseCurrency, targetCurrency);
            return createSuccessResponse(rate);
        } catch (IOException e) {
            return createErrorResponse(HttpStatus.NOT_ACCEPTABLE.toString(), e.getMessage(), null);
        }
    }

    @GetMapping("/convert")
    public RestResourceResponse convertAmountBetweenCurrencies(@RequestParam("from") CurrencyEnum baseCurrency, @RequestParam("to") CurrencyEnum targetCurrency, @RequestParam("amount") BigDecimal amount) {
        try {
            ConversionResponseDto conversion =  exchangeRateService.convertAmountBetweenCurrencies(baseCurrency, targetCurrency, amount);
            return createSuccessResponse(conversion);
        } catch (IOException e) {
            return createErrorResponse(HttpStatus.NOT_ACCEPTABLE.toString(), e.getMessage(), null);
        }
    }
}
