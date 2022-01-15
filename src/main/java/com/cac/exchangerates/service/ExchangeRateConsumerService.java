package com.cac.exchangerates.service;

import com.cac.exchangerates.dto.ExchangeRateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateConsumerService {

    public ExchangeRateDto consumeExchangeRates(){
        final String uri = "http://api.exchangeratesapi.io/v1/latest?access_key=5039dce8ee2957b8a0936edeb0cbf54f";

        RestTemplate restTemplate = new RestTemplate();
        ExchangeRateDto result = restTemplate.getForObject(uri, ExchangeRateDto.class);

        return result;
    }
}
