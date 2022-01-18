package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateConsumerServiceImplTest {
    @InjectMocks
    ExchangeRateConsumerServiceImpl exchangeRateConsumerService;

    @Test
    public void should_consume_exchange_rates() {
        ConsumedRatesDto rates = exchangeRateConsumerService.consumeExchangeRates();
        assertThat(rates).isNotNull();
        assertThat(rates.getRates()).isNotNull();
        assertThat(rates.getRates()).isNotEmpty();
        assertThat(rates.getRates().get(CurrencyEnum.EUR)).isNotNull();
    }
}
