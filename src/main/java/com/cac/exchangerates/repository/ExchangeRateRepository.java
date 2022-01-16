package com.cac.exchangerates.repository;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByBaseCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date);
    ExchangeRate findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date);
}
