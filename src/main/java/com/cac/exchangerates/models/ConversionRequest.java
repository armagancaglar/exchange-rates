package com.cac.exchangerates.models;

import com.cac.exchangerates.dto.ConversionRequestDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name="ConversionRequest")
public class ConversionRequest {
    @Id
    @Column(length = 36)
    private String id;
    private String fromCurrency;
    private String toCurrency;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal conversion;

    public ConversionRequest(){
        setId(UUID.randomUUID().toString());
    }

    public ConversionRequest(String id, String fromCurrency, String toCurrency, LocalDate date, BigDecimal amount, BigDecimal conversion){
        setId(UUID.randomUUID().toString());
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.date = date;
        this.amount = amount;
        this.conversion = conversion;
    }

    public ConversionRequest(ConversionRequestDto conversionRequestDto) {
        setId(UUID.randomUUID().toString());
        this.fromCurrency = conversionRequestDto.getFromCurrency();
        this.toCurrency = conversionRequestDto.getToCurrency();
        this.date = conversionRequestDto.getDate();
        this.amount = conversionRequestDto.getAmount();
        this.conversion = conversionRequestDto.getConversion();
    }

}
