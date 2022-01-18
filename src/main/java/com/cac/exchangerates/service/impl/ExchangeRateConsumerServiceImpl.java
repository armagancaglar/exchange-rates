package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.service.ExchangeRateConsumerService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateConsumerServiceImpl implements ExchangeRateConsumerService {

    /*
    @Value("${exchange.rates.api.access.key}")
    private String accessKey;

    @Value("${exchange.rates.api.url}")
    private String apiURL;
     */

    /**
     * The method is for the retrieve the rates from a service provider
     *
     * @return
     */
    public ConsumedRatesDto consumeExchangeRates() {

        final String uri ="http://api.exchangeratesapi.io/v1/latest?access_key=5039dce8ee2957b8a0936edeb0cbf54f";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, ConsumedRatesDto.class);

        /* This code block for the test purposes because the call request was limited by the service provider
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new ClassPathResource("rates.json").getFile();
        return objectMapper.readValue(jsonFile, ConsumedRatesDto.class);

         */
    }
}
