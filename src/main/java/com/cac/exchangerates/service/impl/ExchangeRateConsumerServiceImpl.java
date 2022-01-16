package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.service.ExchangeRateConsumerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateConsumerServiceImpl implements ExchangeRateConsumerService {

    @Value("${exchange.rates.api.access.key}")
    private String accessKey;

    @Value("${exchange.rates.api.url}")
    private String apiURL;
    /**
     * The method is for the retrieve the rates from a service provider
     * @return
     */
    public ConsumedRatesDto consumeExchangeRates() {
        final String uri = apiURL.concat("?access_key=").concat(accessKey);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, ConsumedRatesDto.class);

        /*

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new ClassPathResource("rates.json").getFile();
        return objectMapper.readValue(jsonFile, ConsumedRatesDto.class);

         */
    }
}
