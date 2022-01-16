package com.cac.exchangerates.dto;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.models.ConversionRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRequestDto {
    private String id;
    private CurrencyEnum baseCurrency;
    private CurrencyEnum targetCurrency;
    private LocalDate date;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal price;

    public ConversionRequestDto(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date, BigDecimal rate,
                                BigDecimal amount, BigDecimal price) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.date = date;
        this.rate = rate;
        this.amount = amount;
        this.price = price;
    }

    public ConversionRequestDto(ConversionRequest conversionRequest) {
        this.id = conversionRequest.getId();
        this.baseCurrency = conversionRequest.getBaseCurrency();
        this.targetCurrency = conversionRequest.getTargetCurrency();
        this.date = conversionRequest.getDate();
        this.rate = conversionRequest.getRate();
        this.amount = conversionRequest.getAmount();
        this.price = conversionRequest.getPrice();
    }
}
