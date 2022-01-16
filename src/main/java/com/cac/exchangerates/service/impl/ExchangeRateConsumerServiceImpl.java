package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.service.ExchangeRateConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ExchangeRateConsumerServiceImpl implements ExchangeRateConsumerService {

    public ConsumedRatesDto consumeExchangeRates() throws IOException {
        /*
        final String uri = "http://api.exchangeratesapi.io/v1/latest?access_key=5039dce8ee2957b8a0936edeb0cbf54f";

        RestTemplate restTemplate = new RestTemplate();
        ExchangeRateDto result = restTemplate.getForObject(uri, ExchangeRateDto.class);


         */

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new ClassPathResource("rates.json").getFile();
        return objectMapper.readValue(jsonFile, ConsumedRatesDto.class);
    }
}
