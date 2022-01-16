package com.cac.exchangerates.service;

import com.cac.exchangerates.dto.ConsumedRatesDto;

import java.io.IOException;

public interface ExchangeRateConsumerService {
    ConsumedRatesDto consumeExchangeRates() throws IOException;
}
