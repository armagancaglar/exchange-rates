package com.cac.exchangerates.service;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {
    ExchangeRate save(ExchangeRateDto exchangeRateDto);
    void retrieveAndSaveExchangeRates() throws IOException;
    ConversionResponseDto convertAmountBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, BigDecimal amount) throws IOException;
    BigDecimal calculateRateBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) throws IOException;
    List<ExchangeRateDto> findByTargetCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date);
    BigDecimal getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date) throws IOException;
    void currencyValidation(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency);
}
