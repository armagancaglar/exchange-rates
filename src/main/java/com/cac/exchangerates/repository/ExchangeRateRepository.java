package com.cac.exchangerates.repository;

import com.cac.exchangerates.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByFromCurrencyCodeAndDate(String currencyCode, LocalDate date);
    ExchangeRate findByFromCurrencyCodeAndToCurrencyCodeAndDate(String fromCurrencyCode, String toCurrencyCode, LocalDate date);
}
