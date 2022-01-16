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

    /**
     * Method for the save the exchange rate to the database
     * Method accepts ExchangeRateDto and creates a new ExchangeRate then saves it
     * @param exchangeRateDto
     * @return
     */
    public ExchangeRate save(ExchangeRateDto exchangeRateDto) {
        if (null != exchangeRateDto) {
            ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);
            return exchangeRateRepository.save(exchangeRate);
        }
        throw new IllegalArgumentException("Exchange Rate can not be null!");
    }

    /**
     * The method is for the retrieving the currency rates from external service provider
     */
    public void retrieveAndSaveExchangeRates() {
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

    /**
     * The method calculates the amount in target currency
     * @param baseCurrency
     * @param targetCurrency
     * @param amount
     * @return
     */
    public ConversionResponseDto convertAmountBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, BigDecimal amount) {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal rate = calculateRateBetweenCurrencies(baseCurrency, targetCurrency);

        BigDecimal conversionPrice = rate.multiply(amount).setScale(6, RoundingMode.HALF_EVEN);
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto(baseCurrency, targetCurrency, LocalDate.now(), rate, amount, conversionPrice);
        conversionRequestDto = conversionRequestServiceImpl.save(conversionRequestDto);

        return new ConversionResponseDto(conversionRequestDto);
    }

    /**
     * The method calculates the rate between base currency and target currency
     * @param baseCurrency
     * @param targetCurrency
     * @return
     */
    public BigDecimal calculateRateBetweenCurrencies(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) {
        currencyValidation(baseCurrency, targetCurrency);

        BigDecimal fromRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(baseCurrency, LocalDate.now());
        BigDecimal toRateWithEUR = getRateByBaseCurrencyAndTargetCurrencyAndDate(targetCurrency, LocalDate.now());

        return toRateWithEUR.divide(fromRateWithEUR, RoundingMode.HALF_EVEN).setScale(6, RoundingMode.HALF_EVEN);
    }

    /**
     * The method is for the retrieving the target currency rate at given date
     * @param currencyCode
     * @param date
     * @return
     */
    @Cacheable
    public List<ExchangeRateDto> findByTargetCurrencyAndDate(CurrencyEnum currencyCode, LocalDate date) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByBaseCurrencyAndDate(currencyCode, date);
        if (null != exchangeRates)
            return exchangeRates.stream().map(ExchangeRateDto::new).collect(Collectors.toList());

        return Collections.emptyList();
    }

    /**
     * The method is for the retrieving rate of the target currency with EUR on given date
     * @param targetCurrency
     * @param date
     * @return
     */
    // The method can take one more arguments which is base currency to get the rates between two currency
    // getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date)
    // At this point baseCurrency argument is not necessary because the data is provided for just EUR
    @Cacheable
    public BigDecimal getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum targetCurrency, LocalDate date) {
        currencyValidation(EUR, targetCurrency);

        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(EUR, targetCurrency, date);
        if (null == exchangeRate) {
            retrieveAndSaveExchangeRates();
            exchangeRate = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(EUR, targetCurrency, date);
        }
        return exchangeRate.getRate();
    }

    /**
     * The methods to check if the currencies are exists
     *
     * @param baseCurrency
     * @param targetCurrency
     */
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
