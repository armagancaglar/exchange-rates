package com.cac.exchangerates.service;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.dto.ConversionResponseDto;
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

import static com.cac.exchangerates.constants.CurrencyEnum.EUR;

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
        List<ExchangeRateDto> exchangeRateDtos = findByTargetCurrencyAndDate(EUR, LocalDate.now());

        if(null == exchangeRateDtos || exchangeRateDtos.isEmpty()) {
            ConsumedRatesDto consumedRatesDto = exchangeRateConsumerService.consumeExchangeRates();

            if(null != consumedRatesDto && null != consumedRatesDto.getRates()) {
                for(Map.Entry<CurrencyEnum, BigDecimal> entry : consumedRatesDto.getRates().entrySet()) {
                    ExchangeRate exchangeRate = new ExchangeRate(consumedRatesDto);
                    exchangeRate.setTargetCurrency(entry.getKey());
                    exchangeRate.setRate(entry.getValue());
                    save(exchangeRate);
                }
            }
        }
    }

    public ConversionResponseDto convertAmountBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, BigDecimal amount) throws IOException {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal rate = calculateRateBetweenCurrencies(baseCurrency, targetCurrency);

        BigDecimal conversionPrice = rate.multiply(amount).setScale(6, RoundingMode.HALF_EVEN);
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto(baseCurrency, targetCurrency, LocalDate.now(), rate, amount, conversionPrice);
        conversionRequestDto = conversionRequestService.save(conversionRequestDto);

        return new ConversionResponseDto(conversionRequestDto);
    }

    public BigDecimal calculateRateBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) throws IOException {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal fromRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(EUR, baseCurrency, LocalDate.now());
        BigDecimal toRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(EUR, targetCurrency, LocalDate.now());

        return toRateWithEUR.divide(fromRateWithEUR, RoundingMode.HALF_EVEN).setScale(6, RoundingMode.HALF_EVEN);
    }

    @Cacheable
    public List<ExchangeRateDto> findByTargetCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date){
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByBaseCurrencyAndDate(currencyCode, date);
        if( null != exchangeRates )
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

    private void currencyValidation(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency){
        HashSet<CurrencyEnum> currencyCodes = CurrencyEnum.getCurrencySet();

        if(baseCurrency == null) {
            throw new IllegalArgumentException("From Currency must be provided!");
        }
        if(targetCurrency == null) {
            throw new IllegalArgumentException("To Currency must be provided!");
        }
        if(!currencyCodes.contains(baseCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", baseCurrency));
        }
        if(!currencyCodes.contains(targetCurrency)) {
            throw new IllegalArgumentException(String.format("%s currency is not supported for now", baseCurrency));
        }
    }

}
