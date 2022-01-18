package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.models.ConversionRequest;
import com.cac.exchangerates.repository.ConversionRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConversionRequestServiceImplTest {
    @Mock
    ConversionRequestRepository conversionRequestRepository;

    @InjectMocks
    ConversionRequestServiceImpl conversionRequestServiceImpl;

    @Test
    public void should_retrieve_conversion_requests_with_pagination_and_date(){
        //ARRANGE
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setRate(new BigDecimal(15));
        conversionRequest.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequest.setDate(LocalDate.now());
        conversionRequest.setAmount(new BigDecimal(100));
        conversionRequest.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequest.setPrice(new BigDecimal(1500));
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);


        when(conversionRequestRepository.findByIdOrDate(null, LocalDate.now(), pageable))
                .thenReturn(new PageImpl(Collections.singletonList(conversionRequest)));

        //ACT
        List<ConversionRequestDto> results = conversionRequestServiceImpl.getConversionRequestByIdOrDate(null, LocalDate.now(), pageable);

        //ASSERT
        verify(conversionRequestRepository).findByIdOrDate(null, LocalDate.now(), pageable);
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getBaseCurrency()).isEqualTo(CurrencyEnum.EUR);
        assertThat(results.get(0).getId()).isEqualTo(conversionRequest.getId());
    }

    @Test
    public void should_retrieve_conversion_requests_with_pagination_and_id(){
        //ARRANGE
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setRate(new BigDecimal(15));
        conversionRequest.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequest.setDate(LocalDate.now());
        conversionRequest.setAmount(new BigDecimal(100));
        conversionRequest.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequest.setPrice(new BigDecimal(1500));
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);


        when(conversionRequestRepository.findByIdOrDate(conversionRequest.getId(), null, pageable))
                .thenReturn(new PageImpl(Collections.singletonList(conversionRequest)));

        //ACT
        List<ConversionRequestDto> results = conversionRequestServiceImpl.getConversionRequestByIdOrDate(conversionRequest.getId(), null, pageable);

        //ASSERT
        verify(conversionRequestRepository).findByIdOrDate(conversionRequest.getId(), null, pageable);
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getBaseCurrency()).isEqualTo(CurrencyEnum.EUR);
        assertThat(results.get(0).getId()).isEqualTo(conversionRequest.getId());
    }

    @Test
    public void should_not_retrieve_conversion_requests_when_id_and_date_is_not_given(){
        //ARRANGE
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.getConversionRequestByIdOrDate(null, null, pageable);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Transaction ID or Transaction Date must be provided!");

    }

    @Test
    public void should_save_conversion_request(){
        //ARRANGE
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        when(conversionRequestRepository.save(conversionRequest)).thenReturn(conversionRequest);

        //ACT
        ConversionRequestDto savedConversionRequest = conversionRequestServiceImpl.save(conversionRequestDto);

        //ASSERT
        verify(conversionRequestRepository).save(conversionRequest);
        assertThat(savedConversionRequest.getId()).isEqualTo(conversionRequestDto.getId());
        assertThat(savedConversionRequest.getBaseCurrency()).isEqualTo(conversionRequestDto.getBaseCurrency());
        assertThat(savedConversionRequest.getPrice()).isEqualTo(conversionRequestDto.getPrice());
        assertThat(savedConversionRequest.getRate()).isEqualTo(conversionRequestDto.getRate());
    }

    @Test
    public void should_not_save_conversion_request_when_request_is_null(){
        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ConversionRequest must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_amount_is_null(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(null);
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_amount_is_less_than_zero(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(-1));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The amount of the conversion can not be less than 0!");
    }

    @Test
    public void should_not_save_conversion_request_when_base_currency_is_null(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(null);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Base Currency must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_target_currency_is_null(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(null);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Target Currency must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_rate_is_null(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(null);
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rate must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_rate_is_less_than_zero(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(-1));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(1500));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The rate can not be less than 0!");
    }

    @Test
    public void should_not_save_conversion_request_when_price_is_null(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(null);
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be provided!");
    }

    @Test
    public void should_not_save_conversion_request_when_price_is_less_than_zero(){
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto();
        conversionRequestDto.setRate(new BigDecimal(15));
        conversionRequestDto.setTargetCurrency(CurrencyEnum.TRY);
        conversionRequestDto.setDate(LocalDate.now());
        conversionRequestDto.setAmount(new BigDecimal(100));
        conversionRequestDto.setBaseCurrency(CurrencyEnum.EUR);
        conversionRequestDto.setPrice(new BigDecimal(-1));
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequestDto.setId(conversionRequest.getId());

        //ACT - ASSERT
        assertThatThrownBy(() -> {
            conversionRequestServiceImpl.save(conversionRequestDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The price can not be less than 0!");
    }
}
