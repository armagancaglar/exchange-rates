package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import com.cac.exchangerates.models.ExchangeRate;
import com.cac.exchangerates.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRatesServiceImplTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;

    @Mock
    ConversionRequestServiceImpl conversionRequestService;

    @Mock
    ExchangeRateConsumerServiceImpl consumerService;

    @InjectMocks
    ExchangeRateServiceImpl exchangeRateService;

    private ExchangeRateDto createTryExchangeRateDto(){
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setRate(new BigDecimal("15.50"));
        exchangeRateDto.setTargetCurrency(CurrencyEnum.TRY);
        exchangeRateDto.setBaseCurrency(CurrencyEnum.EUR);
        exchangeRateDto.setDate(LocalDate.now());
        exchangeRateDto.setTimestamp(Timestamp.from(Instant.now()));
        return exchangeRateDto;
    }

    private ExchangeRateDto createEurExchangeRateDto(){
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setRate(BigDecimal.ONE);
        exchangeRateDto.setTargetCurrency(CurrencyEnum.EUR);
        exchangeRateDto.setBaseCurrency(CurrencyEnum.EUR);
        exchangeRateDto.setDate(LocalDate.now());
        exchangeRateDto.setTimestamp(Timestamp.from(Instant.now()));
        return exchangeRateDto;
    }

    private ConsumedRatesDto createConsumedRatesDto(){
        ConsumedRatesDto consumedRatesDto = new ConsumedRatesDto();
        consumedRatesDto.setDate(LocalDate.now());
        consumedRatesDto.setTimestamp(Timestamp.from(Instant.now()));
        consumedRatesDto.setBase(CurrencyEnum.EUR);
        consumedRatesDto.setSuccess(true);
        consumedRatesDto.setRates(new HashMap<>() {{
            put(CurrencyEnum.EUR, BigDecimal.ONE);
            put(CurrencyEnum.TRY, new BigDecimal("15.4"));
        }});
        return consumedRatesDto;
    }

    @Test
    public void should_save_exchange_rate(){
        //ARRANGE
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setRate(new BigDecimal("15.50"));
        exchangeRateDto.setTargetCurrency(CurrencyEnum.TRY);
        exchangeRateDto.setBaseCurrency(CurrencyEnum.EUR);
        exchangeRateDto.setDate(LocalDate.now());
        exchangeRateDto.setTimestamp(Timestamp.from(Instant.now()));
        ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);

        when(exchangeRateRepository.save(exchangeRate)).thenReturn(exchangeRate);
        //ACT
        ExchangeRate savedRate = exchangeRateService.save(exchangeRateDto);

        //ASSERT
        verify(exchangeRateRepository).save(exchangeRate);
        assertThat(savedRate.getDate()).isEqualTo(exchangeRate.getDate());
        assertThat(savedRate.getId()).isEqualTo(exchangeRate.getId());
        assertThat(savedRate.getRate()).isEqualTo(exchangeRate.getRate());
    }

    @Test
    public void should_not_save_exchange_rate_when_null_parameter_is_given(){
        //ACT - ASSERT
        assertThatThrownBy(() -> {
            exchangeRateService.save(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exchange Rate can not be null!");
    }

    @Test
    public void should_import_exchange_rates() {
        //ARRANGE
        ConsumedRatesDto consumedRatesDto = createConsumedRatesDto();
        ExchangeRateDto tryExchangeRateDto = new ExchangeRateDto(consumedRatesDto);
        tryExchangeRateDto.setTargetCurrency(CurrencyEnum.TRY);
        tryExchangeRateDto.setRate(new BigDecimal("15.4"));
        ExchangeRate tryExchangeRate = new ExchangeRate(tryExchangeRateDto);

        ExchangeRateDto eurExchangeRateDto = new ExchangeRateDto(consumedRatesDto);
        eurExchangeRateDto.setTargetCurrency(CurrencyEnum.EUR);
        eurExchangeRateDto.setRate(BigDecimal.ONE);
        ExchangeRate eurExchangeRate = new ExchangeRate(eurExchangeRateDto);

        when(consumerService.consumeExchangeRates()).thenReturn(consumedRatesDto);
        when(exchangeRateRepository.save(eurExchangeRate)).thenReturn(eurExchangeRate);
        when(exchangeRateRepository.save(tryExchangeRate)).thenReturn(tryExchangeRate);

        //ACT
        exchangeRateService.importExchangeRates();

        //ASSERT
        verify(consumerService).consumeExchangeRates();
        verify(exchangeRateRepository).save(eurExchangeRate);
        verify(exchangeRateRepository).save(tryExchangeRate);
    }

    @Test
    public void should_find_by_target_currency_and_date(){
        //ARRANGE
        ExchangeRateDto exchangeRateDto = createEurExchangeRateDto();
        ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);

        when(exchangeRateRepository.findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now()))
                .thenReturn(Collections.singletonList(exchangeRate));
        //ACT
        List<ExchangeRateDto> exchangeRateList = exchangeRateService.findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now());

        //ASSERT
        verify(exchangeRateRepository).findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now());
        assertThat(exchangeRateList).isNotNull();
        assertThat(exchangeRateList).isNotEmpty();
        assertThat(exchangeRateList.get(0)).isNotNull();
        assertThat(exchangeRateList.get(0).getTargetCurrency()).isNotNull();
        assertThat(exchangeRateList.get(0).getTargetCurrency()).isEqualTo(CurrencyEnum.EUR);

    }

    @Test
    public void should_not_find_any_by_target_currency_and_date(){
        //ARRANGE
        when(exchangeRateRepository.findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now()))
                .thenReturn(null);
        //ACT
        List<ExchangeRateDto> exchangeRateList = exchangeRateService.findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now());

        //ASSERT
        verify(exchangeRateRepository).findByTargetCurrencyAndDate(CurrencyEnum.EUR, LocalDate.now());
        assertThat(exchangeRateList).isEmpty();
    }

    @Test
    public void should_get_rate_by_base_currency_and_target_currency_and_date() {
        //ARRANGE
        ExchangeRateDto exchangeRateDto = createTryExchangeRateDto();
        ExchangeRate exchangeRate = new ExchangeRate(exchangeRateDto);
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now())).thenReturn(exchangeRate);

        //ACT
        BigDecimal rate = exchangeRateService.getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.TRY, LocalDate.now());
        //ASSERT
        assertThat(rate).isEqualTo(exchangeRate.getRate());
    }

    @Test
    public void should_not_get_rate_by_base_currency_and_target_currency_and_date() {
        //ARRANGE
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now())).thenReturn(null);

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            exchangeRateService.getRateByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.TRY, LocalDate.now());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Could not get exchange rates!");

    }


    @Test
    public void should_convert_amount_between_currencies() {
        //ARRANGE
        ExchangeRateDto tryExchangeRateDto = createTryExchangeRateDto();
        ExchangeRate tryExchangeRate = new ExchangeRate(tryExchangeRateDto);

        ExchangeRateDto eurExchangeRateDto = createEurExchangeRateDto();
        ExchangeRate eurExchangeRate = new ExchangeRate(eurExchangeRateDto);

        ConversionRequestDto conversionRequestDto = new ConversionRequestDto(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now(), new BigDecimal("15.500000"), new BigDecimal("100"), new BigDecimal("1550.000000"));

        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now())).thenReturn(tryExchangeRate);
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now())).thenReturn(eurExchangeRate);
        when(conversionRequestService.save(conversionRequestDto)).thenReturn(conversionRequestDto);
        //ACT
        ConversionResponseDto response = exchangeRateService.convertAmountBetweenCurrencies(CurrencyEnum.EUR.getCode(), CurrencyEnum.TRY.getCode(), new BigDecimal("100"));

        //ASSERT
        verify(exchangeRateRepository).findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now());
        verify(exchangeRateRepository).findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now());
        assertThat(response.getAmount()).isEqualTo(new BigDecimal("1550.000000"));
    }

    @Test
    public void should_not_convert_amount_between_not_invalid_currency_codes(){
        //ACT - ASSERT
        assertThatThrownBy(() -> {
            exchangeRateService.convertAmountBetweenCurrencies("Armagan", CurrencyEnum.TRY.getCode(), new BigDecimal("100"));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Armagan currency is not supported");
    }

    @Test
    public void should_not_convert_amount_between_when_currency_code_is_null(){
        //ACT - ASSERT
        assertThatThrownBy(() -> {
            exchangeRateService.convertAmountBetweenCurrencies(null, CurrencyEnum.TRY.getCode(), new BigDecimal("100"));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Currency code can not be empty!");
    }

    @Test
    public void should_not_convert_amount_between_currencies_when_data_not_exist() {
        // ARRANGE
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now())).thenReturn(null);

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            exchangeRateService.convertAmountBetweenCurrencies(CurrencyEnum.EUR.getCode(), CurrencyEnum.TRY.getCode(), new BigDecimal("100"));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Could not get exchange rates!");

        //ASSERT
        verify(exchangeRateRepository, Mockito.times(2)).findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now());
    }

    @Test
    public void should_calculate_rate_between_currencies() {
        //ARRANGE
        ExchangeRateDto tryExchangeRateDto = createTryExchangeRateDto();
        ExchangeRate tryExchangeRate = new ExchangeRate(tryExchangeRateDto);

        ExchangeRateDto eurExchangeRateDto = createEurExchangeRateDto();
        ExchangeRate eurExchangeRate = new ExchangeRate(eurExchangeRateDto);

        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now())).thenReturn(tryExchangeRate);
        when(exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now())).thenReturn(eurExchangeRate);
        //ACT
        BigDecimal rate = exchangeRateService.calculateRateBetweenCurrencies(CurrencyEnum.EUR.getCode(), CurrencyEnum.TRY.getCode());

        //ASSERT
        verify(exchangeRateRepository).findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.TRY, LocalDate.now());
        verify(exchangeRateRepository).findByBaseCurrencyAndTargetCurrencyAndDate(CurrencyEnum.EUR, CurrencyEnum.EUR, LocalDate.now());
        assertThat(rate).isEqualTo(new BigDecimal("15.500000"));
    }


}
