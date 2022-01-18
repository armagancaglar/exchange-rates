package com.cac.exchangerates.service;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {
    ExchangeRate save(ExchangeRateDto exchangeRateDto);
    void importExchangeRates();
    ConversionResponseDto convertAmountBetweenCurrencies(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
    BigDecimal calculateRateBetweenCurrencies(String baseCurrencyCode, String targetCurrencyCode);
    List<ExchangeRateDto> findByTargetCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date);
    BigDecimal getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum targetCurrency, LocalDate date);
}
