package com.cac.exchangerates.service;

import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;
import com.cac.exchangerates.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateConsumerService exchangeRateConsumerService;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, ExchangeRateConsumerService exchangeRateConsumerService){
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateConsumerService = exchangeRateConsumerService;
    }

    public ExchangeRate save(ExchangeRateDto exchangeRateDto) {
        if ( null != exchangeRateDto) {
            ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);
            return exchangeRateRepository.save(exchangeRate);
        }
        throw new IllegalArgumentException("Exchange Rate can not be null!");
    }

    

    public ExchangeRateDto retrieveAndSaveExchangeRates(){
        return new ExchangeRateDto();
    }

    @Cacheable("exchangeRate")
    public ExchangeRateDto findByCurrency(String currency){
        ExchangeRate exchangeRate = exchangeRateRepository.findByCurrency(currency);
        if( null != exchangeRate )
            return new ExchangeRateDto(exchangeRate);

        throw new EntityNotFoundException("Exchange Rates can not found with given currency!");
    }

}
