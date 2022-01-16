package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;
import com.cac.exchangerates.repository.ExchangeRateRepository;
import com.cac.exchangerates.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.cac.exchangerates.constants.CurrencyEnum.EUR;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateConsumerServiceImpl exchangeRateConsumerServiceImpl;
    private final ConversionRequestServiceImpl conversionRequestServiceImpl;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, ExchangeRateConsumerServiceImpl exchangeRateConsumerServiceImpl, ConversionRequestServiceImpl conversionRequestServiceImpl) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateConsumerServiceImpl = exchangeRateConsumerServiceImpl;
        this.conversionRequestServiceImpl = conversionRequestServiceImpl;
    }

    public ExchangeRate save(ExchangeRateDto exchangeRateDto) {
        if (null != exchangeRateDto) {
            ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);
            return exchangeRateRepository.save(exchangeRate);
        }
        throw new IllegalArgumentException("Exchange Rate can not be null!");
    }

    public void retrieveAndSaveExchangeRates() throws IOException {
        List<ExchangeRateDto> exchangeRateDtos = findByTargetCurrencyAndDate(EUR, LocalDate.now());

        if (null == exchangeRateDtos || exchangeRateDtos.isEmpty()) {
            ConsumedRatesDto consumedRatesDto = exchangeRateConsumerServiceImpl.consumeExchangeRates();

            if (null != consumedRatesDto && null != consumedRatesDto.getRates()) {
                for (Map.Entry<CurrencyEnum, BigDecimal> entry : consumedRatesDto.getRates().entrySet()) {
                    ExchangeRateDto exchangeRateDto = new ExchangeRateDto(consumedRatesDto);
                    exchangeRateDto.setTargetCurrency(entry.getKey());
                    exchangeRateDto.setRate(entry.getValue());
                    save(exchangeRateDto);
                }
            }
        }
    }

    public ConversionResponseDto convertAmountBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, BigDecimal amount) throws IOException {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal rate = calculateRateBetweenCurrencies(baseCurrency, targetCurrency);

        BigDecimal conversionPrice = rate.multiply(amount).setScale(6, RoundingMode.HALF_EVEN);
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto(baseCurrency, targetCurrency, LocalDate.now(), rate, amount, conversionPrice);
        conversionRequestDto = conversionRequestServiceImpl.save(conversionRequestDto);

        return new ConversionResponseDto(conversionRequestDto);
    }

    public BigDecimal calculateRateBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) throws IOException {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal fromRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(EUR, baseCurrency, LocalDate.now());
        BigDecimal toRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(EUR, targetCurrency, LocalDate.now());

        return toRateWithEUR.divide(fromRateWithEUR, RoundingMode.HALF_EVEN).setScale(6, RoundingMode.HALF_EVEN);
    }

    @Cacheable
    public List<ExchangeRateDto> findByTargetCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByBaseCurrencyAndDate(currencyCode, date);
        if (null != exchangeRates)
            return exchangeRates.stream().map(ExchangeRateDto::new).collect(Collectors.toList());

        return Collections.emptyList();
    }

    @Cacheable
    public BigDecimal getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date) throws IOException {
        currencyValidation(baseCurrency, targetCurrency);

        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(baseCurrency, targetCurrency, date);
        if (null == exchangeRate) {
            retrieveAndSaveExchangeRates();
            exchangeRate = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(baseCurrency, targetCurrency, date);
        }
        return exchangeRate.getRate();
    }

    public void currencyValidation(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) {
        HashSet<CurrencyEnum> currencyCodes = CurrencyEnum.getCurrencySet();
        if (baseCurrency == null) {
            throw new IllegalArgumentException("From Currency must be provided!");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("To Currency must be provided!");
        }
        if (!currencyCodes.contains(baseCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", baseCurrency));
        }
        if (!currencyCodes.contains(targetCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", baseCurrency));
        }
    }
}
