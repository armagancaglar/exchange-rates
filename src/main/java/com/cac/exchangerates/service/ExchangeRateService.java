package com.cac.exchangerates.service;

import com.cac.exchangerates.constants.CurrencyCodes;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;
import com.cac.exchangerates.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.cac.exchangerates.constants.CurrencyCodes.EUR;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateConsumerService exchangeRateConsumerService;
    private final ConversionRequestService conversionRequestService;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, ExchangeRateConsumerService exchangeRateConsumerService, ConversionRequestService conversionRequestService){
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateConsumerService = exchangeRateConsumerService;
        this.conversionRequestService = conversionRequestService;
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        if ( null != exchangeRate) {
            return exchangeRateRepository.save(exchangeRate);
        }
        throw new IllegalArgumentException("Exchange Rate can not be null!");
    }

    public void retrieveAndSaveExchangeRates() throws IOException {
        List<ExchangeRateDto> exchangeRateDtos = findByFromCurrencyCodeAndDate(EUR.getCode(), LocalDate.now());

        if(null == exchangeRateDtos || exchangeRateDtos.isEmpty()) {
            ConsumedRatesDto consumedRatesDto = exchangeRateConsumerService.consumeExchangeRates();

            if(null != consumedRatesDto && null != consumedRatesDto.getRates()) {
                for(Map.Entry<String, BigDecimal> entry : consumedRatesDto.getRates().entrySet()) {
                    ExchangeRate exchangeRate = new ExchangeRate(consumedRatesDto);
                    exchangeRate.setToCurrencyCode(entry.getKey());
                    exchangeRate.setRate(entry.getValue());
                    save(exchangeRate);
                }
            }
        }
    }

    public BigDecimal convertAmountBetweenCurrencies(String fromCurrency, String toCurrency, BigDecimal amount) throws IOException {
        BigDecimal rate = calculateRateBetweenCurrencies(fromCurrency, toCurrency);

        BigDecimal conversionPrice = rate.multiply(amount).setScale(6, RoundingMode.CEILING);
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto(fromCurrency, toCurrency, LocalDate.now(), amount, conversionPrice);
        conversionRequestService.save(conversionRequestDto);

        return conversionPrice;
    }

    public BigDecimal calculateRateBetweenCurrencies(String fromCurrency, String toCurrency) throws IOException {
        HashSet<String> currencyCodes = CurrencyCodes.getCurrencyCodeSet();
        if(!currencyCodes.contains(fromCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", fromCurrency));
        }

        if(!currencyCodes.contains(toCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", fromCurrency));
        }

        BigDecimal eurRate = getRateByFromCurrencyCodeAndToCurrencyCodeAndDate(EUR.getCode(), EUR.getCode(), LocalDate.now());
        BigDecimal fromRateWithEUR = getRateByFromCurrencyCodeAndToCurrencyCodeAndDate(EUR.getCode(), fromCurrency, LocalDate.now());
        BigDecimal toRateWithEUR = getRateByFromCurrencyCodeAndToCurrencyCodeAndDate(EUR.getCode(), toCurrency, LocalDate.now());

        BigDecimal firstRate = eurRate.divide(fromRateWithEUR, RoundingMode.CEILING);
        return firstRate.multiply(toRateWithEUR);

    }

    @Cacheable
    public List<ExchangeRateDto> findByFromCurrencyCodeAndDate(String currencyCode, LocalDate date){
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByFromCurrencyCodeAndDate(currencyCode, date);
        if( null != exchangeRates )
            return exchangeRates.stream().map(ExchangeRateDto::new).collect(Collectors.toList());

        return Collections.emptyList();
    }

    @Cacheable
    public BigDecimal getRateByFromCurrencyCodeAndToCurrencyCodeAndDate(String fromCurrencyCode, String toCurrencyCode, LocalDate date) throws IOException {
        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyCodeAndToCurrencyCodeAndDate(fromCurrencyCode, toCurrencyCode, date);
        if (null == exchangeRate) {
            retrieveAndSaveExchangeRates();
            exchangeRate = exchangeRateRepository.findByFromCurrencyCodeAndToCurrencyCodeAndDate(fromCurrencyCode, toCurrencyCode, date);
        }
        return exchangeRate.getRate();
    }

}
