package com.cac.exchangerates.models;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConversionRequestDto;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name="ConversionRequest", indexes = @Index(columnList = "id, baseCurrency, date, targetCurrency"))
public class ConversionRequest {
    @Id
    @Column(length = 36)
    private String id;
    private CurrencyEnum baseCurrency;
    private CurrencyEnum targetCurrency;
    private LocalDate date;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal price;

    public ConversionRequest(){
        setId(UUID.randomUUID().toString());
    }

    public ConversionRequest(String id, CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, LocalDate date, BigDecimal rate, BigDecimal amount, BigDecimal price){
        setId(UUID.randomUUID().toString());
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.date = date;
        this.rate = rate;
        this.amount = amount;
        this.price = price;
    }

    public ConversionRequest(ConversionRequestDto conversionRequestDto) {
        setId(UUID.randomUUID().toString());
        this.baseCurrency = conversionRequestDto.getBaseCurrency();
        this.targetCurrency = conversionRequestDto.getTargetCurrency();
        this.date = conversionRequestDto.getDate();
        this.rate = conversionRequestDto.getRate();
        this.amount = conversionRequestDto.getAmount();
        this.price = conversionRequestDto.getPrice();
    }

}
