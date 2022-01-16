package com.cac.exchangerates.dto;

import com.cac.exchangerates.models.ConversionRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRequestDto {
    private String id;
    private String fromCurrency;
    private String toCurrency;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal conversion;

    public ConversionRequestDto(String fromCurrency, String toCurrency, LocalDate date, BigDecimal amount, BigDecimal conversion) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.date = date;
        this.amount = amount;
        this.conversion = conversion;
    }

    public ConversionRequestDto(ConversionRequest conversionRequest) {
        this.id = conversionRequest.getId();
        this.fromCurrency = conversionRequest.getFromCurrency();
        this.toCurrency = conversionRequest.getToCurrency();
        this.date = conversionRequest.getDate();
        this.amount = conversionRequest.getAmount();
        this.conversion = conversionRequest.getConversion();
    }
}
